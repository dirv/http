package com.danielirvine.http.contributors;

import com.danielirvine.http.Request;
import com.danielirvine.http.Response;
import com.danielirvine.http.ResponseCode;
import com.danielirvine.http.resources.ErrorResponse;

public class NotFoundResponseContributor implements ResponseContributor {

  private static final Response notFoundResponse = new ErrorResponse(ResponseCode.NOT_FOUND);

  @Override
  public boolean canRespond(Request request) {
    return true;
  }

  @Override
  public Response respond(Request request) {
    return notFoundResponse;
  }
}
