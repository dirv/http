package com.danielirvine.http;

import java.util.List;

class DeleteResponseContributor implements ResponseContributor {

  private final DirectoryResource root;
  private final List<String> writeables;

  public DeleteResponseContributor(DirectoryResource root, List<String> writeables) {
    this.root = root;
    this.writeables = writeables;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.isDelete() && root.hasResource(request.getPathSegments());
  }

  @Override
  public Response response(Request request) {
    if(!writeables.contains(request.getPath())){
      return new ErrorResponse(ResponseCode.METHOD_NOT_ALLOWED);
    }
    root.findResource(request.getPathSegments()).delete();
    return new EmptyResponse(ResponseCode.OK);
  }

}
