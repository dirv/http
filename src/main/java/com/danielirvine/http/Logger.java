package com.danielirvine.http;

import java.util.*;

class Logger {

  private final List<Request> requests = new ArrayList<Request>();

  public void log(Request request) {
    requests.add(request);
  }

  public List<Request> entries() {
    return Collections.unmodifiableList(requests);
  }
}
