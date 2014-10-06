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

  public Resource findResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return this;
    } else {
      FileDescriptor child = descriptor.getFile(pathSegments[1]);
      if(child == null) {
        return new NotFoundResource();
      } else {
        return new FileResource(child);
      }
    }
  }
}
