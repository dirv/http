package com.danielirvine.http.contributors;

import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.responses.EmptyResponse;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class WriteableResponseContributor extends PathMatchingResponseContributor {

  public WriteableResponseContributor(List<String> writeablePaths) {
    super(writeablePaths);
  }

  @Override
  public Response respond(Request request) {
    return new EmptyResponse(ResponseCode.OK);
  }
}
