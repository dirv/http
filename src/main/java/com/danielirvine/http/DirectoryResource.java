package com.danielirvine.http;

import java.io.*;
import java.util.*;
import static java.util.Arrays.*;

public class DirectoryResource implements Resource {

  private final FileDescriptor descriptor;

  public DirectoryResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public ResponseCode getResponseCode() {
    return ResponseCode.OK;
  }

  public void dumpResource(OutputStream out) {
    try{
      for(FileDescriptor child : descriptor.getChildren()) {
        out.write(createLink(child.getName()).getBytes());
        out.write(HttpServer.CRLF.getBytes());
      }
    } catch(IOException ex) {
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

  public List<ResponseHeader> getHeaders() {
    return asList();
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }

  private String createLink(String text) {
    return "<a href=\"/" + text + "\">" + text + "</a>";
  }
}
