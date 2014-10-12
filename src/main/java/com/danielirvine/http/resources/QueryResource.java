package com.danielirvine.http.resources;
import java.io.*;
import java.util.*;

import com.danielirvine.http.content.*;

import static java.util.stream.Collectors.*;

public class QueryResource implements Resource {

  private final Map<String, String> variables;
  private final long lastModified;

  public QueryResource(Map<String, String> variables) {
    this.variables = variables;
    this.lastModified = System.currentTimeMillis();
  }

  public Content toContent() {
    return new ListContent(variables.entrySet()
      .stream()
      .map(this::toQueryVariableContent)
      .collect(toList()));
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

  @Override
  public long lastModified() {
    return lastModified;
  }

  @Override
  public String getETag() {
    // TODO Auto-generated method stub
    return null;
  }
}

