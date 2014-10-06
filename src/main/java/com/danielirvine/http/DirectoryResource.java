package com.danielirvine.http;

import java.io.*;
import java.util.*;

public class DirectoryResource implements Resource {

  private final FileDescriptor descriptor;

  public DirectoryResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public ResponseCode getResponseCode() {
    return ResponseCode.OK;
  }

  public void dumpResource(PrintWriter out) {
    for(FileDescriptor child : descriptor.getChildren()) {
      out.write(child.getName());
      out.write(HttpServer.CRLF);
    }
  }
}
