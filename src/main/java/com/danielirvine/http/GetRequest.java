package com.danielirvine.http;

public class GetRequest {

  private final String target;

  public GetRequest(String requestLine) {
    String[] parts = requestLine.split(" ");
    target = parts[1];
  }

  public String getTarget() {
    return target;
  }

}
