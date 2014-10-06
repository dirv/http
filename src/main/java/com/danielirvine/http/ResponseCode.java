package com.danielirvine.http;

public enum ResponseCode {
  
  OK(200, "OK"),
  NOT_FOUND(404, "Not Found");

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
