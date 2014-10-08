package com.danielirvine.http;

class UnauthorizedResponseContributor implements ResponseContributor {

  private final Authorizor authorizor;

  public UnauthorizedResponseContributor(Authorizor authorizor) {
    this.authorizor = authorizor;
  }

  @Override
  public boolean canRespond(Request request) {
    return authorizor.requiresAuthorization(request.getPath());
  }

  @Override
  public Response response(Request request) {
    String realm = request.getPath();
    return new ErrorResponse(ResponseCode.UNAUTHORIZED,
        new ResponseHeader("WWW-Authenticate",
          "Basic realm=\"" + realm + "\""));
  }
}
