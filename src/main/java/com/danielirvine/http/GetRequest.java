package com.danielirvine.http;

public class GetRequest {

  private final String target;
  private final FileDescriptor publicRoot;

  public GetRequest(String requestLine, FileDescriptor publicRoot) {
    String[] parts = requestLine.split(" ");
    this.target = parts[1];
    this.publicRoot = publicRoot;
  }

  public boolean targetExists() {
    String fileName = stripRootDirectory(target);
    if (fileName.equals("")) {
      return true;
    } else {
      return publicRoot.getFile(fileName) != null;
    }
  }

  private String stripRootDirectory(String target) {
    return target.substring(1);
  }
}
