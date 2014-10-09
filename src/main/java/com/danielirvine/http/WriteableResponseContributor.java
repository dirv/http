package com.danielirvine.http;

import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

class WriteableResponseContributor extends PathMatchingResponseContributor {

  public WriteableResponseContributor(List<String> writeablePaths) {
    super(writeablePaths);
  }

  @Override
  public Response response(Request request) {
    return new EmptyResponse(ResponseCode.OK);
  }
}
