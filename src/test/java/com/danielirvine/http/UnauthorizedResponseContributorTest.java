package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.io.*;
import java.net.*;
import java.util.function.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

public class UnauthorizedResponseContributorTest {

  private final Base64.Encoder encoder = Base64.getEncoder();
  private UnauthorizedResponseContributor unauthContributor = new UnauthorizedResponseContributor(AuthorizerTest.AUTHORIZER);
  private String requestContent;

  @Test
	public void authorizesCorrectUser() {
    startRequest("GET /a HTTP/1.1");
    String credentials = "admin:" + AuthorizerTest.PLAIN_TEXT_PASSWORD;
    addHeader("Authorization",
        "Basic " + encoder.encodeToString(credentials.getBytes()));
    assertFalse(unauthContributor.canRespond(buildRequest()));
  }

  private void startRequest(String requestLine) {
    requestContent = requestLine + HttpServer.CRLF;
  }

  private void addHeader(String name, String value) {
    requestContent += name;
    requestContent += ": ";
    requestContent += value;
    requestContent += HttpServer.CRLF;
  }

  private Request buildRequest() {
    requestContent += HttpServer.CRLF;
    try {
    return new Request(new StringBufferInputStream(requestContent));
    } catch(Exception ex) {
      return null;
    }
  }
}
