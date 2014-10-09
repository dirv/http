package com.danielirvine.http.resources;
import java.io.*;
import java.util.*;

import com.danielirvine.http.Response;
import com.danielirvine.http.ResponseCode;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.HtmlHeadedContent;
import com.danielirvine.http.content.QueryVariableContent;
import com.danielirvine.http.headers.request.RangeHeader;

import static java.util.stream.Collectors.*;

public class QueryResource implements Resource {

  private final Map<String, String> variables;

  public QueryResource(Map<String, String> variables) {
    this.variables = variables;
  }

  public Response toResponse() {
    return new Response(ResponseCode.OK, new HtmlHeadedContent(generateContent()));
  }

  public Resource applyRange(RangeHeader range) {
    // TODO: Could actually apply a range at this point since
    // the content has been generated (or at least could be)
    return this;
  }

  private List<Content> generateContent() {
    return variables.entrySet()
      .stream()
      .map(this::toQueryVariableContent)
      .collect(toList());
  }

  private QueryVariableContent toQueryVariableContent(Map.Entry<String, String> variable) {
    return new QueryVariableContent(variable.getKey(), variable.getValue());
  }

  @Override
  public void write(Reader in) {
  }

  @Override
  public void delete() {
  }
}

