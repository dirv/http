package com.danielirvine.http;

import java.io.*;
import java.net.*;
import java.util.function.*;

public class HttpServer {

  public static final String CRLF = "\r\n";
  public static final String PROTOCOL_VERSION = "HTTP/1.1";
  private final DirectoryResource root;
  private final UrlRedirects redirects;

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory, int port, String publicRoot, InputStream redirectStream) {
    this(socketFactory.apply(port), new FsFileDescriptor(new File(publicRoot)), redirectStream);
  }

  public HttpServer(ServerSocketProxy socket, FileDescriptor rootFile, InputStream redirectsStream) {
    root = new DirectoryResource(rootFile);
    redirects = new UrlRedirects(redirectsStream);
    while(socket.hasData()) {
      try(SocketProxy clientSocket = socket.accept()) {
        handleIncomingRequest(clientSocket);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser = new ArgumentParser(args);

    InputStream redirectStream = HttpServer.class.getResourceAsStream("/redirects.txt");
    new HttpServer(HttpServer::createSocket,
        parser.get("p", 5000),
        parser.get("d", ""),
        redirectStream);
  }

  private void handleIncomingRequest(SocketProxy socket) throws IOException {
    Request request = new Request(socket.getInputStream(), root, redirects);
    Response response = request.response();
    response.write(socket.getOutputStream());
  }

  private static ServerSocketProxy createSocket(int port) {
    try {
      return new NetServerSocket(port);
    }
    catch(IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

}
