package com.danielirvine.http;

import java.net.*;
import java.util.function.*;
import java.io.*;
import java.net.*;

public class HttpServer {

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory, int port) {
    // TODO: get rid of this Function interface, possibly just pass in an already constructed socket.
    try {
      ServerSocketProxy socket = socketFactory.apply(port);
      SocketProxy clientSocket = socket.accept();
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        if(inputLine.startsWith("GET /")) {
          out.println("HTTP/1.1 200 OK");
          clientSocket.close();
          break;
        }
      }
    }
    catch(Exception ex) {
      System.out.println(ex);
    }
  }

  public static void main(String[] args) {

    new HttpServer(HttpServer::createSocket, new ArgumentParser(args).getInt("p", 5000));
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
