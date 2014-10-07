package com.danielirvine.http;

import java.io.*;
import java.util.concurrent.*;

public class InProcessServerSocket implements ServerSocketProxy, Closeable {

  private final ConcurrentLinkedQueue<String> inputs = new ConcurrentLinkedQueue<String>();
  private final ConcurrentLinkedQueue<String> outputs = new ConcurrentLinkedQueue<String>();

  public InProcessServerSocket(String[] inputs) {
    for(String input : inputs) {
      this.inputs.add(input);
    }
  }

  public boolean hasData() {
    return !inputs.isEmpty();
  }

  public SocketProxy accept() throws IOException {
    return new SocketProxy() {
      String thisInput = inputs.poll();

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(thisInput.getBytes());
      }

      public OutputStream getOutputStream() throws IOException {
        return outputStream;
      }

      public void close() throws IOException {
        outputs.add(outputStream.toString());
      }
    };
  }

  public String getOutput(int i) {
    return outputs.toArray(new String[0])[i];
  }

  public void close() throws IOException {
  }
}
