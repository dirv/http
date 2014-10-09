package com.danielirvine.http;

import java.util.*;

class WriteableResponseContributor extends PathMatchingResponseContributor {

  public WriteableResponseContributor(List<String> writeablePaths) {
    super(writeablePaths);
  }

  @Override
  public Response respond(Request request) {
    return new EmptyResponse(ResponseCode.OK);
  }
}
