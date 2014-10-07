package com.danielirvine.http;

import java.io.*;
import java.net.*;
import java.util.function.*;

public class HttpServer {

  public static final String CRLF = "\r\n";

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory, int port, String publicRoot) {
    this(socketFactory.apply(port), new FsFileDescriptor(new File(publicRoot)));
  }

  public HttpServer(ServerSocketProxy socket, FileDescriptor rootFile) {
    DirectoryResource root = new DirectoryResource(rootFile);
    try {
      SocketProxy clientSocket = socket.accept();
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      GetRequest getRequest = new GetRequest(clientSocket.getInputStream(), root);
      getRequest.response().print(out);
      clientSocket.close();
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
