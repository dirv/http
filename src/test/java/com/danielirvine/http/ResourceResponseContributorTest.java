package com.danielirvine.http;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;

import org.junit.Test;

import com.danielirvine.http.content.StringContent;
import com.danielirvine.http.contributors.*;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.responses.Response;

public class ResourceResponseContributorTest extends RequestTest {

  private final InMemoryResourceCache cache = new InMemoryResourceCache();
  private final InMemoryFileDescriptor root = new InMemoryFileDescriptor("/");
  private final DirectoryResource publicRoot = new DirectoryResource(root);
  private final ResourceResponseContributor contributor = new ResourceResponseContributor(publicRoot, cache);

  @Test
  public void savesFileInCache() {
    root.addFile("file", "Hello, world!");
    startRequest("GET /file HTTP/1.1");

    contributor.respond(buildRequest());
    assertTrue(cache.hasContent("/file"));
  }
  
  @Test
  public void respondsCorrectlyToCachedFile() {
    cache.store("/file", new StringContent("Hello, world!"));
    startRequest("GET /file HTTP/1.1");
    Response response = contributor.respond(buildRequest());

    assertThat(responseText(response), hasItem(containsString("Hello, world!")));
  }
}
