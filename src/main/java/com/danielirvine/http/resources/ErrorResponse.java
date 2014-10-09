package com.danielirvine.http.resources;

import static java.util.Arrays.*;

import com.danielirvine.http.Response;
import com.danielirvine.http.ResponseCode;
import com.danielirvine.http.content.HeadedContent;
import com.danielirvine.http.content.StringContent;
import com.danielirvine.http.headers.response.ResponseHeader;

public class ErrorResponse extends Response {
  public ErrorResponse(ResponseCode code, ResponseHeader... headers) {
    super(code, new HeadedContent(
          asList(headers),
          asList(new StringContent(code.toString()))));
  }
}
