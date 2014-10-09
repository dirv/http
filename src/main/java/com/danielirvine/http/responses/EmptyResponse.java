package com.danielirvine.http.responses;

import static java.util.Arrays.*;

import com.danielirvine.http.content.HeadedContent;

public class EmptyResponse extends Response {
  public EmptyResponse(ResponseCode code) {
    super(code, new HeadedContent(asList(), asList()));
  }
}
