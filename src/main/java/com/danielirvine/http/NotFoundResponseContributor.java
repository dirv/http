package com.danielirvine.http;

class NotFoundResponseContributor implements ResponseContributor {

  private static final Response notFoundResponse = new ErrorResponse(ResponseCode.NOT_FOUND);

  @Override
  public boolean canRespond(Request request) {
    return true;
  }

  @Override
  public Response response(Request request) {
    return notFoundResponse;
  }
}
