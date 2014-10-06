package com.danielirvine.http;

import java.net.*;
import java.util.function.*;
import java.io.*;
import java.net.*;

public class HttpServer {

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
          out.print("HTTP/1.1 200 OK\r\n");
          out.print("\r\n");
          getRequest.dumpResource(out);
        }
        else {
          out.print("HTTP/1.1 404 Not Found\r\n");
          out.print("\r\n");
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
}
