package com.danielirvine.http.contributors;

import com.danielirvine.http.*;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.resources.Resource;
import com.danielirvine.http.responses.Response;

public class ResourceResponseContributor implements ResponseContributor {

  private final DirectoryResource root;

  public ResourceResponseContributor(DirectoryResource root) {
    this.root = root;
  }

  @Override
  public boolean canRespond(Request request) {
    return root.findResource(request.getPathSegments()) != null;
  }

  @Override
  public Response respond(Request request) {
    Resource resource = root.findResource(request.getPath().split("/"));
    if(request.hasRanges()) {
      resource = resource.applyRange(request.getRanges());
    }
    return resource.toResponse();
  }
}
