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
    assertEquals(ResponseCode.OK, contributor.response(buildRequest()).getResponseCode());
  }
  
  @Test
  public void savesAResource() {
    startRequest("POST /a HTTP/1.1");
    addData("Hello");
    contributor.response(buildRequest());
    assertTrue(directory.getFile("a").exists());
    assertEquals("Hello", readStream(directory.getFile("a").getReadStream()));
  }
  
  private String readStream(InputStream s) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
    int b;
    while((b = s.read()) != -1) {
      out.write(b);
    }
    } catch (IOException ex) {
    }
    return out.toString();
  }
}
