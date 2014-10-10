package com.danielirvine.http.responses;

import com.danielirvine.http.content.StringContent;

public class EmptyResponse extends Response {
  public EmptyResponse(ResponseCode code) {
    super(code, new StringContent(""));
  }
}
