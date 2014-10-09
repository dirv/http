package com.danielirvine.http;
import java.io.*;
import java.util.*;
import static java.util.stream.Collectors.*;

class QueryResource implements Resource {

  private final Map<String, String> variables;

  public QueryResource(Map<String, String> variables) {
    this.variables = variables;
  }

  public Response toResponse() {
    return new Response(ResponseCode.OK, new PlainTextHeadedContent(generateContent()));
  }

  public Resource applyRange(RangeHeader range) {
    // TODO: Could actually apply a range at this point since
    // the content has been generated (or at least could be)
    return this;
  }

  public void write(Reader in) {
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
}

