package com.danielirvine.http;

import java.util.*;

class WriteableResponseContributor extends PathMatchingResponseContributor {

  public WriteableResponseContributor(List<String> writeablePaths) {
    super(writeablePaths);
  }

  @Override
  public Response response(Request request) {
    return new EmptyResponse(ResponseCode.OK);
  }
}
