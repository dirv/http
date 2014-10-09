package com.danielirvine.http;

class ResourceResponseContributor implements ResponseContributor {

  private final DirectoryResource root;

  public ResourceResponseContributor(DirectoryResource root) {
    this.root = root;
  }

  @Override
  public boolean canRespond(Request request) {
    return root.findResource(request.getPathSegments()) != null;
  }

  @Override
  public Response response(Request request) {
    Resource resource = root.findResource(request.getPath().split("/"));
    for(RequestHeader h : request.getHeaders()) {
      resource = h.apply(resource);
    }
    return resource.toResponse();
  }
}
