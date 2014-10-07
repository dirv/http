package com.danielirvine.http;

public class UnknownRequestHeader implements RequestHeader {
  public Resource apply(Resource resource) {
    return resource;
  }
}
