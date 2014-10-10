package com.danielirvine.http;

import static org.junit.Assert.*;

import org.junit.Test;

import com.danielirvine.http.contributors.*;
import com.danielirvine.http.resources.FileResource;
import com.danielirvine.http.responses.Response;

import static org.hamcrest.CoreMatchers.*;

public class InMemoryResourceResponseContributorTest extends RequestTest {

  private final InMemoryResourceCache cache = new InMemoryResourceCache();
  private final InMemoryResourceResponseContributor contributor = new InMemoryResourceResponseContributor(cache);
  private final InMemoryFileDescriptor descriptor = new InMemoryFileDescriptor("test", "Hello, world!");
  private final FileResource file = new FileResource(descriptor);

  @Test
  public void doesNotRespondToFileWhichIsNotCached() {
    startRequest("GET /file HTTP/1.1");
    assertFalse(contributor.canRespond(buildRequest()));
  }
  
  @Test
  public void canRespondToCachedFile() {
    cache.store("/file", file.toContent());
    startRequest("GET /file HTTP/1.1");
    assertTrue(contributor.canRespond(buildRequest()));
  }
  
  
  @Test
  public void respondsCorrectlyToCachedFile() {
    cache.store("/file", file.toContent());
    startRequest("GET /file HTTP/1.1");
    Response response = contributor.respond(buildRequest());

    assertThat(responseText(response), hasItem(containsString("Hello, world!")));
  }
  
}
