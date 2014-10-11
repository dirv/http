package com.danielirvine.http.responses;

import com.danielirvine.http.headers.response.ResponseHeader;

public class EmptyResponse extends Response {
  public EmptyResponse(ResponseCode code) {
    super(code, ResponseHeader.EMPTY);
  }
}
