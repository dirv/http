package com.danielirvine.http;

import java.io.*;

class FileResource implements Resource {

  private final FileDescriptor descriptor;

  public FileResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public ResponseCode getResponseCode() {
    return ResponseCode.OK;
  }

  public void dumpResource(PrintWriter out) {
    try {
      InputStream reader = descriptor.getReadStream();
      int b;
      while((b = reader.read()) != -1) {
        out.write(b);
      }
    } catch(IOException ex) {
      System.err.println(ex);
    }
  }
}
