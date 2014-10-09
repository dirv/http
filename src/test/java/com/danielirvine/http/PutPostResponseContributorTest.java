package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

public class PutPostResponseContributorTest extends ResponseContributorTest {

  private final InMemoryFileDescriptor directory = new InMemoryFileDescriptor("/");
  private final DirectoryResource root = new DirectoryResource(directory);
  private final PutPostResponseContributor contributor = new PutPostResponseContributor(root);

  @Test
	public void respondToPut() {
    startRequest("PUT /a HTTP/1.1");
    assertTrue(contributor.canRespond(buildRequest()));
  }

  @Test
	public void respondToPost() {
    startRequest("POST /a HTTP/1.1");
    assertTrue(contributor.canRespond(buildRequest()));
  }

  @Test
	public void doesNotRespondToGet() {
    startRequest("GET /a HTTP/1.1");
    assertFalse(contributor.canRespond(buildRequest()));
  }

  @Test
	public void returnsOk() {
    startRequest("POST /a HTTP/1.1");
    assertEquals(ResponseCode.OK, contributor.response(buildRequest()).getResponseCode());
  }
}
