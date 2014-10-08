package com.danielirvine.http;

import java.util.*;
import static java.util.Arrays.*;

class ErrorResource implements Resource {

  private final Response response;

  public ErrorResource(ResponseCode code, ResponseHeader... headers) {
    this.response = new Response(code,
        new HeadedContent(
          asList(headers),
          asList(new StringContent(code.toString()))));

  }
  public Response toResponse() {
    return response;
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }
}
