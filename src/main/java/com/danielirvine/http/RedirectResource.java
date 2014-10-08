package com.danielirvine.http;

class RedirectResource extends ErrorResource {

  public RedirectResource(String location) {
    super(ResponseCode.MOVED_PERMANENTLY,
          new ResponseHeader("Location", location));
  }
}
