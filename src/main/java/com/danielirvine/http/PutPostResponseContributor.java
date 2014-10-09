package com.danielirvine.http;

class PutPostResponseContributor implements ResponseContributor {

  private final DirectoryResource root;

  public PutPostResponseContributor(DirectoryResource root) {
    this.root = root;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.isPut() || request.isPost();
  }

  @Override
  public Response response(Request request) {
    return new EmptyResponse(ResponseCode.OK);
  }
}
