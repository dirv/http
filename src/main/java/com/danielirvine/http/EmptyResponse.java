package com.danielirvine.http;

import static java.util.Arrays.*;

class EmptyResponse extends Response {
  public EmptyResponse(ResponseCode code) {
    super(code, new HeadedContent(asList(), asList()));
  }
}
