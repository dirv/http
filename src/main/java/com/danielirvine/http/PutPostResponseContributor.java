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
  public Response response(Request request) {
    if(!writeablePaths.contains(request.getPath())) {
      return new ErrorResponse(ResponseCode.METHOD_NOT_ALLOWED);
    }

    Resource child = root.findOrCreateResource(request.getPathSegments());
    child.write(request.getDataStream());

    return new EmptyResponse(ResponseCode.OK);
  }

  private String[] getParent(String[] pathSegments) {
    if(pathSegments.length == 0) return new String[0];
    return Arrays.copyOfRange(pathSegments, 0, pathSegments.length - 1);
  }
}
