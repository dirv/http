package com.danielirvine.http;

import static java.util.Arrays.*;

class ErrorResponse extends Response {
  public ErrorResponse(ResponseCode code, ResponseHeader... headers) {
    super(code, new HeadedContent(
          asList(headers),
          asList(new StringContent(code.toString()))));
  }
}
