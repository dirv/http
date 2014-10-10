package com.danielirvine.http;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import com.danielirvine.http.contributors.*;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.resources.FileResource;
import com.danielirvine.http.responses.Response;

public class ResourceResponseContributorTest extends RequestTest {

  private final InMemoryResourceCache cache = new InMemoryResourceCache();
  private final InMemoryFileDescriptor root = new InMemoryFileDescriptor("/");
  private final DirectoryResource publicRoot = new DirectoryResource(root);
  private final ResourceResponseContributor contributor = new ResourceResponseContributor(publicRoot, cache);

  @Test
  public void savesFileInCache() {
    FileResource file = new FileResource(root.addFile("file", "Hello, world!"));
    startRequest("GET /file HTTP/1.1");

    contributor.respond(buildRequest());
    assertTrue(cache.hasCurrentContent("/file", file));
  }
  
  @Test
  public void respondsCorrectlyToCachedFile() {
    FileResource file = new FileResource(root.addFile("file", "Hello, world!"));
    cache.store("/file", file.toContent());
    startRequest("GET /file HTTP/1.1");
    Response response = contributor.respond(buildRequest());

    assertThat(responseText(response), hasItem(containsString("Hello, world!")));
  }
  
  @Test
  public void doesNotServeCachedResourceIfUnderlyingResourceHasChanged() {
    InMemoryFileDescriptor file = root.addFile("file", "Hello, world!");
    startRequest("GET /file HTTP/1.1");
    contributor.respond(buildRequest());
    file.write(new StringReader("Hello, everybody!"));
    startRequest("GET /file HTTP/1.1");
    Response response = contributor.respond(buildRequest());
    assertThat(responseText(response), hasItem(containsString("Hello, everybody!")));
  }
}
