package com.danielirvine.http.responses;

import com.danielirvine.http.content.HeadedContent;
import com.danielirvine.http.content.StringContent;
import com.danielirvine.http.headers.response.ResponseHeader;

import static java.util.Arrays.*;

public class ErrorResponse extends Response {
  public ErrorResponse(ResponseCode code, ResponseHeader... headers) {
    super(code, new HeadedContent(asList(headers), new StringContent(code.toString())));
  }
}
