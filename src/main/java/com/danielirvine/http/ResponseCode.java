package com.danielirvine.http;

public enum ResponseCode {

  OK(200, "OK"),
  PARTIAL(206, "Partial content"),
  UNSATISFIABLE(216, "Requested range not satisfiable"),
  MOVED_PERMANENTLY(301, "Moved permanently"),
  UNAUTHORIZED(401, "Authentication required"),
  NOT_FOUND(404, "Not Found"),
  METHOD_NOT_ALLOWED(405, "Method not allowed");

  private final int code;
  private final String description;

  private ResponseCode(int code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public String toString() {
    return "" + code + " " + description;
  }
}
