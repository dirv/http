package com.danielirvine.http;

public class GetRequest {

  private final String target;
  private final FileDescriptor publicRoot;

  public GetRequest(String requestLine, FileDescriptor publicRoot) {
    String[] parts = requestLine.split(" ");
    this.target = parts[1];
    this.publicRoot = publicRoot;
  }

  public Response response() {
    Resource resource = buildResource();
    ResponseCode code = resource.getResponseCode();
    return new Response(code, resource);
  }

  private Resource buildResource() {
    String fileName = stripRootDirectory(target);
    if (fileName.equals("")) {
      return new RootResource();
    }
    FileDescriptor file = publicRoot.getFile(fileName);
    if(file == null) {
      return new NotFoundResource();
    }
    return new FileResource(file);
  }

  private String stripRootDirectory(String target) {
    return target.substring(1);
  }
}
