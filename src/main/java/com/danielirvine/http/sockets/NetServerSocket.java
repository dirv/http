package com.danielirvine.http.sockets;

import java.net.*;
import java.io.*;

import static com.danielirvine.http.ExceptionWrapper.*;

public class NetServerSocket implements ServerSocketProxy {

  private final ServerSocket socket;

  public NetServerSocket(int port) {
    this.socket = decheck(() -> new ServerSocket(port));
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

  public boolean hasData() {
    return true;
  }

  public void close() throws IOException {
    socket.close();
  }
}
