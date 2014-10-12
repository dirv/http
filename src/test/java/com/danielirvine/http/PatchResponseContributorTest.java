package com.danielirvine.http;

import org.junit.*;

import com.danielirvine.http.contributors.PatchResponseContributor;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.resources.FileResource;
import com.danielirvine.http.responses.ResponseCode;

import static org.junit.Assert.*;

import java.util.*;

public class PatchResponseContributorTest extends RequestTest {

  private final InMemoryFileDescriptor directory = new InMemoryFileDescriptor("/");
  private final DirectoryResource root = new DirectoryResource(directory);
  private final List<String> writeables = Arrays.asList("/a");
  private final PatchResponseContributor contributor = new PatchResponseContributor(root, writeables, new InMemoryResourceCache());

  @Test
	public void respondToPatch() {
    directory.addFile("a", "Hello, world!");
    startRequest("PATCH /a HTTP/1.1");
    assertTrue(contributor.canRespond(buildRequest()));
  }

  @Test
  public void returnsPreconditionFailed() {
    directory.addFile("a", "Hello, world!");
    startRequest("PATCH /a HTTP/1.1");
    assertEquals(ResponseCode.PRECONDITION_FAILED, contributor.respond(buildRequest()).getResponseCode());
  }

  @Test
	public void returnsOk() {
    FileDescriptor file = directory.addFile("a", "Hello, world!");
    String eTag = new FileResource(file).getETag();
    startRequest("PATCH /a HTTP/1.1");
    addHeader("If-Match", eTag);
    assertEquals(ResponseCode.NO_CONTENT, contributor.respond(buildRequest()).getResponseCode());
  }
  
  @Test
  public void savesAResource() {
    FileDescriptor file = directory.addFile("a", "Hello, world!");
    String eTag = new FileResource(file).getETag();
    startRequest("PATCH /a HTTP/1.1");
    addHeader("If-Match", eTag);
    addHeader("Content-Length", "13");
    addData("Hi everybody!");
    contributor.respond(buildRequest());
    assertEquals("Hi everybody!", readStream(directory.getFile("a").getReadStream()));
  }
}
