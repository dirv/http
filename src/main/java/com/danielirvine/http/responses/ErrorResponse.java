package com.danielirvine.http.responses;

import com.danielirvine.http.content.StringContent;
import com.danielirvine.http.headers.response.ResponseHeader;

public class ErrorResponse extends Response {
  public ErrorResponse(ResponseCode code, ResponseHeader... headers) {
    super(code, new StringContent(code.toString()));
  }
}
