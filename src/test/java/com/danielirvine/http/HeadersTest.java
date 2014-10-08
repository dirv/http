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
  private Request request;
  private Response response;


  @Test
	public void sendsContentTypeForJpeg() {
    rootDirectory.addFile("test.jpeg", "unknown");
    buildRequestWithHeader("test.jpeg", "");
    assertThat(headers(), hasItem(containsString("Content-type: image/jpeg")));
  }

  @Test
	public void sendsContentLengthForJpeg() {
    rootDirectory.addFile("test.jpeg", "unknown");
    buildRequestWithHeader("test.jpeg", "");
    assertThat(headers(), hasItem(containsString("Content-Length: 7")));
  }

  @Test
	public void rangeShowsMultipartByteRangesHeader() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    buildRequestWithHeader("alphabet", "Range: bytes=0-10,-5");
    assertThat(headers(), hasItem(containsString("Content-type: multipart/byteranges; boundary=")));
  }

  @Test
	public void rangeShowsOriginalContentPart() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    buildRequestWithHeader("alphabet", "Range: bytes=-5");
    assertThat(headers(), hasItem(containsString("Content-type: text/plain")));
    assertThat(headers(), hasItem(containsString("Content-Length: 5")));
  }

  @Test
	public void rangeShowsByteRange() {
    rootDirectory.addFile("alphabet", "abcdefghijklmnopqrstuvwxyz");
    buildRequestWithHeader("alphabet", "Range: bytes=-5");
    assertThat(headers(), hasItem(containsString("Content-range: bytes 21-25/26")));
  }

  private void buildRequestWithHeader(String resource, String header) {
    String requestString = "GET /" + resource + " HTTP/1.1" + HttpServer.CRLF;
    requestString += header + HttpServer.CRLF;
    try {
      Request request = new Request(new StringBufferInputStream(requestString));
      response = new ResourceResponseContributor(root).response(request);
    } catch(IOException ex) {
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
