package com.danielirvine.http;

import java.io.*;

public class InProcessServerSocket implements ServerSocketProxy, Closeable {

  private final String input;
  private String output;

  public InProcessServerSocket(String input) {
    this.input = input;
  }

  public SocketProxy accept() throws IOException {
    return new SocketProxy() {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(input.getBytes());
      }

      public OutputStream getOutputStream() throws IOException {
        return outputStream;
      }

      public void close() throws IOException {
        output = outputStream.toString();
      }
    };
  }

  public String getOutput() {
    return output;
  }

  public void close() throws IOException {
  }
}
