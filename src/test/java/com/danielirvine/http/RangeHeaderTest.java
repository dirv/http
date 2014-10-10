package com.danielirvine.http;

import org.junit.*;

import com.danielirvine.http.contributors.ResourceResponseContributor;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.*;
import java.util.*;

public class RangeHeaderTest extends RequestTest {

  private static final String content = "abcdefghijklmnopqrstuvwxyz";
  private final InMemoryFileDescriptor root = new InMemoryFileDescriptor("/");
  private final DirectoryResource publicRoot = new DirectoryResource(root);
  private final ResourceResponseContributor contributor = new ResourceResponseContributor(publicRoot);
  private Response response;

  @Test
  public void rangeOfFirstBytes() {
    setRangeOnFile("bytes=0-2");
    assertThat(dumpResource(), hasItem(containsString("abc")));
  }

  @Test
	public void invalidRange() {
    setRangeOnFile("bytes=2-0");
    assertThat(dumpResource(), hasItem(containsString(content)));
    assertEquals(ResponseCode.OK, responseCode());
  }

  @Test
	public void multipleRanges() {
    setRangeOnFile("bytes=0-2,4-5");
    List<String> output = dumpResource();
    assertThat(output, hasItem(containsString("abc")));
    assertThat(output, hasItem(containsString("ef")));
  }

  @Test
	public void openRange() {
    setRangeOnFile("bytes=20-");
    assertThat(dumpResource(), hasItem(containsString("uvwxyz")));
  }

  @Test
  public void overruningRange() {
    setRangeOnFile("bytes=20-30");
    assertThat(dumpResource(), hasItem(containsString("uvwxyz")));
  }

  @Test
  public void gibberishRange() {
    setRangeOnFile("bytes=2030");
    assertThat(dumpResource(), hasItem(containsString(content)));
    assertEquals(ResponseCode.OK, responseCode());
  }

  @Test
	public void suffixRange() {
    setRangeOnFile("bytes=-5");
    assertThat(dumpResource(), hasItem(containsString("vwxyz")));
    assertEquals(ResponseCode.PARTIAL, responseCode());
  }

  @Test
	public void overrunningSuffixRange() {
    setRangeOnFile("bytes=-50");
    assertThat(dumpResource(), hasItem(containsString(content)));
  }

  @Test
	public void zeroSuffixRange() {
    setRangeOnFile("bytes=-0");
    assertEquals(ResponseCode.UNSATISFIABLE, responseCode());
  }

  @Test
	public void overLengthRange() {
    setRangeOnFile("bytes=30-40");
    assertEquals(ResponseCode.UNSATISFIABLE, responseCode());
  }


  private void setRangeOnFile(String rangeHeader) {
    startRequest("GET /alphabet HTTP/1.1");
    addHeader("Range", rangeHeader);
    Request request = buildRequest();
    root.addFile("alphabet", content);
    response = contributor.respond(request);
  }

  private List<String> dumpResource() {
    ByteArrayOutputStream s = new ByteArrayOutputStream();
    try{
      response.write(s);
    } catch(IOException ex) {
    }
    return Arrays.asList(s.toString().split(HttpServer.CRLF));
  }

  private ResponseCode responseCode() {
    return response.getResponseCode();
  }
}

