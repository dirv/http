package com.danielirvine.http.contributors;

import java.util.*;

import com.danielirvine.http.Request;

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
