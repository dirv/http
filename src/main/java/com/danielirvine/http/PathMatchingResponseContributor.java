package com.danielirvine.http;

import java.util.*;

abstract class PathMatchingResponseContributor implements ResponseContributor {

  private final List<String> matchingPaths;

  public PathMatchingResponseContributor(List<String> matchingPaths) {
    this.matchingPaths = matchingPaths;
  }

  @Override
  public boolean canRespond(Request request) {
    return matchingPaths.contains(request.getPath());
  }
}
