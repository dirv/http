package com.danielirvine.http;

import org.junit.*;

import com.danielirvine.http.contributors.UnauthorizedResponseContributor;

import static org.junit.Assert.*;

import java.util.*;

public class UnauthorizedResponseContributorTest extends RequestTest {

  private final Base64.Encoder encoder = Base64.getEncoder();
  private UnauthorizedResponseContributor unauthContributor = new UnauthorizedResponseContributor(AuthorizerTest.AUTHORIZER);

  @Test
	public void authorizesCorrectUser() {
    startRequest("GET /a HTTP/1.1");
    addAuthHeader("admin", AuthorizerTest.PLAIN_TEXT_PASSWORD);
    assertFalse(unauthContributor.canRespond(buildRequest()));
  }

  @Test
	public void doesNotAuthorizeBadPassword() {
    startRequest("GET /a HTTP/1.1");
    addAuthHeader("admin", "oops");
    assertTrue(unauthContributor.canRespond(buildRequest()));
  }

  private void addAuthHeader(String user, String password) {
    String credentials = user + ":" + password;
    String encoded = encoder.encodeToString(credentials.getBytes());
    addHeader("Authorization", "Basic " + encoded);
  }

}
