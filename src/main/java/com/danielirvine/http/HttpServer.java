package com.danielirvine.http;

import java.net.*;
import java.util.function.*;
import java.io.*;
import java.net.*;

public class HttpServer {

  public static final String CRLF = "\r\n";

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory, int port, String publicRoot) {
    this(socketFactory.apply(port), new FsFileDescriptor(new File(publicRoot)));
  }

  public HttpServer(ServerSocketProxy socket, FileDescriptor publicRoot) {
    try {
      SocketProxy clientSocket = socket.accept();
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {

        GetRequest getRequest = new GetRequest(inputLine, publicRoot);
        if(getRequest.targetExists()) {
          addStatusLine(out, ResponseCode.OK);
          endHeader(out);
          getRequest.dumpResource(out);
        }
        else {
          addStatusLine(out, ResponseCode.NOT_FOUND);
          endHeader(out);
        }
        out.flush();
        clientSocket.close();
        break;
      }
    }
    catch(Exception ex) {
      System.out.println(ex);
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser = new ArgumentParser(args);
    new HttpServer(HttpServer::createSocket,
        parser.getInt("p", 5000),
        parser.getString("d", ""));
  }

  private static ServerSocketProxy createSocket(int port) {
    try {
      return new NetServerSocket(port);
    }
    catch(IOException ex) {
      System.err.println(ex);
    }
    return null;
  }

  private void addStatusLine(PrintWriter out, ResponseCode responseCode) {
    out.print("HTTP/1.1 " + responseCode + CRLF);
  }

  private void endHeader(PrintWriter out) {
    out.print(CRLF);
  }
}
