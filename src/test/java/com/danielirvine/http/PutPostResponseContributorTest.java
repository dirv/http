package com.danielirvine.http;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

public class PutPostResponseContributorTest extends RequestTest {

  private final InMemoryFileDescriptor directory = new InMemoryFileDescriptor("/");
  private final DirectoryResource root = new DirectoryResource(directory);
  private final List<String> writeables = Arrays.asList("/a");
  private final PutPostResponseContributor contributor = new PutPostResponseContributor(root, writeables);

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
    assertEquals(ResponseCode.OK, contributor.respond(buildRequest()).getResponseCode());
  }
  
  @Test
  public void savesAResource() {
    startRequest("POST /a HTTP/1.1");
    addHeader("Content-Length", "5");
    addData("Hello");
    contributor.respond(buildRequest());
    assertTrue(directory.getFile("a").exists());
    assertEquals("Hello", readStream(directory.getFile("a").getReadStream()));
  }
}
