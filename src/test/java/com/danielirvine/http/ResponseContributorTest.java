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
  
  protected void addData(String content) {
    requestContent += HttpServer.CRLF;
    requestContent += content;
  }

  protected Request buildRequest() {
    try {
      return new Request(new BufferedReader(new StringReader(requestContent)));
    } catch(Exception ex) {
      return null;
    }
  }
}
