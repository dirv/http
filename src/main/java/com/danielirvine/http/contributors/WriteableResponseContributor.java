package com.danielirvine.http.contributors;

import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.resources.EmptyResponse;

public class WriteableResponseContributor extends PathMatchingResponseContributor {

  public WriteableResponseContributor(List<String> writeablePaths) {
    super(writeablePaths);
  }

  @Override
  public Response respond(Request request) {
    return new EmptyResponse(ResponseCode.OK);
  }
}
