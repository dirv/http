package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

public class AuthorizerTest {

  public static final String AUTH_TABLE = "/a:admin:9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
  public static final String PLAIN_TEXT_PASSWORD = "test";
  public static final Authorizor AUTHORIZER = new Authorizor(new StringBufferInputStream(AUTH_TABLE));

  @Test
	public void pathRequiresAuthorization() {
    assertTrue(AUTHORIZER.requiresAuthorization("/a"));
  }

  @Test
	public void authorizesUser() {
    assertTrue(AUTHORIZER.isAuthorized("/a", "admin", PLAIN_TEXT_PASSWORD));
  }

  @Test
	public void doesNotAuthorizeBadUsername() {
    assertFalse(AUTHORIZER.isAuthorized("/a", "admin2", PLAIN_TEXT_PASSWORD));
  }

  @Test
	public void doesNotAuthorizeBadPassword() {
    assertFalse(AUTHORIZER.isAuthorized("/a", "admin", "test2"));
  }
}
