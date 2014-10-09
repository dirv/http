package com.danielirvine.http.resources;
import java.io.*;
import java.util.*;

import com.danielirvine.http.content.*;
import com.danielirvine.http.ranges.Range;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

import static java.util.stream.Collectors.*;

public class QueryResource implements Resource {

  private final Map<String, String> variables;

  public QueryResource(Map<String, String> variables) {
    this.variables = variables;
  }

  public Response toResponse() {
    return new Response(ResponseCode.OK, new HtmlHeadedContent(generateContent()));
  }

  public Resource applyRange(List<Range> range) {
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

