package com.danielirvine.http;

import java.net.*;
import java.io.*;

public class NetServerSocket implements ServerSocketProxy {

  private final ServerSocket socket;

  public NetServerSocket(int port) throws IOException {
    this.socket = new ServerSocket(port);
  }

  public SocketProxy accept() throws IOException {
    Socket clientSocket = socket.accept();

    return new SocketProxy() {
      public InputStream getInputStream() throws IOException {
        return clientSocket.getInputStream();
      }

      public OutputStream getOutputStream() throws IOException {
        return clientSocket.getOutputStream();
      }

      public void close() throws IOException {
        clientSocket.close();
      }
    };
  }

  public void close() throws IOException {
    socket.close();
  }
}
