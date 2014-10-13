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
  public void sha1MatchesExpectedSha1() {
    FileDescriptor file = directory.addFile("a", "default content\n");
    String eTag = new FileResource(file).getETag();
    assertEquals("60bb224c68b1ed765a0f84d910de58d0beea91c4", eTag);
    file = directory.addFile("b", "patched content\n");
    eTag = new FileResource(file).getETag();
    assertEquals("69bc18dc1edc9e1316348b2eaaca9df83898249f", eTag);
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
