package com.danielirvine.http;

import java.util.*;

class PutPostResponseContributor implements ResponseContributor {

  private final DirectoryResource root;
  private final List<String> writeablePaths;

  public PutPostResponseContributor(DirectoryResource root, List<String> writeablePaths) {
    this.root = root;
    this.writeablePaths = writeablePaths;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.isPut() || request.isPost();
  }

  @Override
  public Response respond(Request request) {
    if(!writeablePaths.contains(request.getPath())) {
      return new ErrorResponse(ResponseCode.METHOD_NOT_ALLOWED);
    }

    Resource child = root.findOrCreateResource(request.getPathSegments());
    child.write(request.getDataStream());

    return new EmptyResponse(ResponseCode.OK);
  }
}
