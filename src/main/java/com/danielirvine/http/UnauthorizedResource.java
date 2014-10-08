package com.danielirvine.http;

import static java.util.Arrays.*;

class UnauthorizedResource implements Resource {

  private final String realm;

  public UnauthorizedResource(String realm) {
    this.realm = realm;
  }

  public Response toResponse() {
    return new Response(ResponseCode.UNAUTHORIZED,
        new HeadedContent(
          asList(new ResponseHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"")),
          asList()));
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }
}
