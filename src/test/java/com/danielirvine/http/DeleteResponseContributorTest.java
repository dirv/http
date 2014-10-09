package com.danielirvine.http;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.danielirvine.http.contributors.DeleteResponseContributor;
import com.danielirvine.http.resources.DirectoryResource;

public class DeleteResponseContributorTest extends RequestTest {

  private final InMemoryFileDescriptor directory = new InMemoryFileDescriptor("/");
  private final DirectoryResource root = new DirectoryResource(directory);
  private final List<String> writeables = Arrays.asList("/a");
  private final DeleteResponseContributor contributor = new DeleteResponseContributor(root, writeables);

  @Test
  public void respondToPut() {
    directory.addFile("a", "to delete");
    startRequest("DELETE /a HTTP/1.1");
    assertTrue(contributor.canRespond(buildRequest()));
  }
}
