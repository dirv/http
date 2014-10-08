package com.danielirvine.http;

import static java.util.Arrays.*;

class RedirectResource implements Resource {

  private final String location;

  public RedirectResource(String location) {
    this.location = location;
  }

  public Response toResponse() {
    return new Response(ResponseCode.MOVED_PERMANENTLY,
        new HeadedContent(
          asList(new ResponseHeader("Location", location)),
          asList()));
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }
}
