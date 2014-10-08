package com.danielirvine.http;

class UnauthorizedResource extends ErrorResource {

  public UnauthorizedResource(String realm) {
    super(ResponseCode.UNAUTHORIZED,
        new ResponseHeader("WWW-Authenticate",
          "Basic realm=\"" + realm + "\""));
  }
}
