package com.danielirvine.http.contributors;

import java.util.*;

import com.danielirvine.http.*;

public class QueryResponseContributor implements ResponseContributor {

  @Override
  public boolean canRespond(Request request) {
    return request.hasQuery();
  }

  @Override
  public Response respond(Request request) {
    return new QueryResource(buildVariables(request)).toResponse();
  }

  private static Map<String, String> buildVariables(Request request) {
    Map<String, String> variables = new HashMap<String, String>();
    String[] keyValues = request.getQuery().split("&");
    for(String keyValue : keyValues) {
      String[] parts = keyValue.split("=");
      variables.put(parts[0], parts[1]);
    }
    return variables;
  }
}
