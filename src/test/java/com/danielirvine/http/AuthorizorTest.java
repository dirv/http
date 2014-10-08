package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

public class AuthorizorTest {

  private final String authTable = "/a:admin:9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
  private final Authorizor a = new Authorizor(new StringBufferInputStream(authTable));

  @Test
	public void pathRequiresAuthorization() {
    assertTrue(a.requiresAuthorization("/a"));
  }

  @Test
	public void authorizesUser() {
    assertTrue(a.isAuthorized("/a", "admin", "test"));
  }

  @Test
	public void doesNotAuthorizeBadUsername() {
    assertFalse(a.isAuthorized("/a", "admin2", "test"));
  }

  @Test
	public void doesNotAuthorizeBadPassword() {
    assertFalse(a.isAuthorized("/a", "admin", "test2"));
  }
}
