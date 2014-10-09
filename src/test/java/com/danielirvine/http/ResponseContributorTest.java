package com.danielirvine.http;

import java.io.*;

public abstract class ResponseContributorTest {
  private String requestContent;

  protected void startRequest(String requestLine) {
    requestContent = requestLine + HttpServer.CRLF;
  }

  protected void addHeader(String name, String value) {
    requestContent += name;
    requestContent += ": ";
    requestContent += value;
    requestContent += HttpServer.CRLF;
  }

  protected Request buildRequest() {
    requestContent += HttpServer.CRLF;
    try {
    return new Request(new StringBufferInputStream(requestContent));
    } catch(Exception ex) {
      return null;
    }
  }
}
