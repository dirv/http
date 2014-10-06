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
      out.write(createLink(child.getName()));
      out.write(HttpServer.CRLF);
    }
  }

  public Resource findResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return this;
    } else {
      FileDescriptor child = descriptor.getFile(pathSegments[1]);
      if(child.exists()) {
        return new FileResource(child);
      } else {
        return new NotFoundResource();
      }
    }
  }

  private String createLink(String text) {
    return "<a href=\"/" + text + "\">" + text + "</a>";
  }
}
