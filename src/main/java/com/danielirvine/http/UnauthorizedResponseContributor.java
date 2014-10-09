package com.danielirvine.http;

class UnauthorizedResponseContributor implements ResponseContributor {

  private final Authorizor authorizor;

  public UnauthorizedResponseContributor(Authorizor authorizor) {
    this.authorizor = authorizor;
  }

  @Override
  public boolean canRespond(Request request) {
    String path = request.getPath();
    if (authorizor.requiresAuthorization(path)) {
      if(request.hasCredentials()) {
        String user = request.getUser();
        String password = request.getPassword();
        if(authorizor.isAuthorized(path, user, password)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public Response respond(Request request) {
    String realm = request.getPath();
    return new ErrorResponse(ResponseCode.UNAUTHORIZED,
        new ResponseHeader("WWW-Authenticate",
          "Basic realm=\"" + realm + "\""));
  }
}
