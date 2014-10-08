package com.danielirvine.http;

class NotFoundResource extends ErrorResource {

  public NotFoundResource() {
    super(ResponseCode.NOT_FOUND);
  }
}
