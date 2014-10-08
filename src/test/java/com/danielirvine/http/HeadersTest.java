package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

public class HeadersTest {

  private final InMemoryFileDescriptor rootDirectory = new InMemoryFileDescriptor("/");
  private final DirectoryResource root = new DirectoryResource(rootDirectory);
  private final UrlRedirects urlRedirects = new UrlRedirects(new StringBufferInputStream(""));
  private final Authorizor authorizor = new Authorizor(new StringBufferInputStream(""));
  private Request request;
  private Response response;


  @Test
	public void sendsContentTypeForJpeg() {
    rootDirectory.addFile("test.jpeg", "unknown");
    request = buildRequestWithHeader("test.jpeg", "");
    response = request.response();
    assertThat(headers(), hasItem(containsString("Content-type: image/jpeg")));
  }

  @Test
	public void sendsContentLengthForJpeg() {
    rootDirectory.addFile("test.jpeg", "unknown");
    request = buildRequestWithHeader("test.jpeg", "");
    response = request.response();
    assertThat(headers(), hasItem(containsString("Content-Length: 7")));
  }

  @Test
	public void rangeShowsMultipartByteRangesHeader() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    request = buildRequestWithHeader("alphabet", "Range: bytes=0-10,-5");
    response = request.response();
    assertThat(headers(), hasItem(containsString("Content-type: multipart/byteranges; boundary=")));
  }

  @Test
	public void rangeShowsOriginalContentPart() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    request = buildRequestWithHeader("alphabet", "Range: bytes=-5");
    response = request.response();
    assertThat(headers(), hasItem(containsString("Content-type: text/plain")));
    assertThat(headers(), hasItem(containsString("Content-Length: 5")));
  }

  @Test
	public void rangeShowsByteRange() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    request = buildRequestWithHeader("alphabet", "Range: bytes=-5");
    response = request.response();
    assertThat(headers(), hasItem(containsString("Content-range: bytes 21-25/26")));
  }

  private Request buildRequestWithHeader(String resource, String header) {
    String request = "GET /" + resource + " HTTP/1.1" + HttpServer.CRLF;
    request += header + HttpServer.CRLF;
    try {
      return new Request(new StringBufferInputStream(request), root, urlRedirects, authorizor);
    } catch(IOException ex) {
      return null;
    }
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
