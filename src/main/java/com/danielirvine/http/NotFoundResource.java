package com.danielirvine.http;

import static java.util.Arrays.*;

class NotFoundResource implements Resource {

  public Response toResponse() {
    return new Response(ResponseCode.NOT_FOUND,
        new PlainTextHeadedContent(
          asList(new StringContent("404 Not Found"))));
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }
}
