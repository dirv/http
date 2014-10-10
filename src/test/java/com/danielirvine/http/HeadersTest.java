package com.danielirvine.http;

import org.junit.*;

import com.danielirvine.http.contributors.ResourceResponseContributor;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.responses.Response;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.*;
import java.util.*;

public class HeadersTest extends RequestTest {

  private final InMemoryFileDescriptor rootDirectory = new InMemoryFileDescriptor("/");
  private final DirectoryResource root = new DirectoryResource(rootDirectory);
  private final InMemoryResourceCache cache = new InMemoryResourceCache();
  private Response response;

  @Test
	public void sendsContentTypeForJpeg() {
    rootDirectory.addFile("test.jpeg", "unknown");
    buildRequestWithHeader("/test.jpeg");
    assertThat(headers(), hasItem(containsString("Content-type: image/jpeg")));
  }

  @Test
	public void sendsContentLengthForJpeg() {
    rootDirectory.addFile("test.jpeg", "unknown");
    buildRequestWithHeader("/test.jpeg");
    assertThat(headers(), hasItem(containsString("Content-Length: 7")));
  }

  @Test
	public void rangeShowsMultipartByteRangesHeader() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    buildRequestWithHeader("/alphabet", "Range", "bytes=0-10,-5");
    assertThat(headers(), hasItem(containsString("Content-type: multipart/byteranges; boundary=")));
  }

  @Test
	public void rangeShowsOriginalContentPart() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    buildRequestWithHeader("/alphabet", "Range", "bytes=-5");
    List<String> headers = headers();
    assertThat(headers, hasItem(containsString("Content-type: text/plain")));
    assertThat(headers, hasItem(containsString("Content-Length: 5")));
  }

  @Test
	public void rangeShowsByteRange() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    buildRequestWithHeader("/alphabet", "Range", "bytes=-5");
    assertThat(headers(), hasItem(containsString("Content-range: bytes 21-25/26")));
  }

  @Test
  public void doesNotReadContentWithoutContentLengthHeader() {
    startRequest("POST /a HTTP/1.1");
    addHeader("Content-Length", "5");
    addData("Hello, world!");
    assertEquals("Hello", readStream(buildRequest().getDataStream()));

  }

  private void buildRequestWithHeader(String resource) {
    startRequest("GET " + resource + " HTTP/1.1");
    response = new ResourceResponseContributor(root, cache).respond(buildRequest());
  }

  private void buildRequestWithHeader(String resource, String name, String value) {
    startRequest("GET " + resource + " HTTP/1.1");
    addHeader(name, value);
    response = new ResourceResponseContributor(root, cache).respond(buildRequest());
  }

  private List<String> headers() {
    ByteArrayOutputStream s = new ByteArrayOutputStream();
    try{
      response.write(s);
    } catch(IOException ex)
    {
    }
    return Arrays.asList(s.toString().split(HttpServer.CRLF));
  }
}
