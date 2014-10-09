package com.danielirvine.http;

import java.util.*;

class Responder {

  private final List<ResponseContributor> contributors;

  public Responder(List<ResponseContributor> contributors) {
    this.contributors = contributors;
  }

  public Response response(Request request) {
    return contributors.stream()
      .filter(c->c.canRespond(request))
      .findFirst()
      .get()
      .respond(request);
  }
}
