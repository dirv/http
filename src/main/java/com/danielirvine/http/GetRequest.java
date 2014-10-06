package com.danielirvine.http;

public class GetRequest {

  private final String target;
  private final DirectoryResource root;

  public GetRequest(String requestLine, DirectoryResource root) {
    String[] parts = requestLine.split(" ");
    this.target = parts[1];
    this.root = root;
  }

  public Response response() {
    Resource resource = buildResource();
    ResponseCode code = resource.getResponseCode();
    return new Response(code, resource);
  }

  private Resource buildResource() {
    return root.findResource(target.split("/"));
  }
}
