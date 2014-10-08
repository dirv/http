package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.*;
import java.util.*;

public class RangeHeaderTest {

  private static final String content = "abcdefghijklmnopqrstuvwxyz";
  private final FileResource file = new FileResource(new InMemoryFileDescriptor("test", content));
  private RangeHeader range;
  private Resource partial;

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
    assertThat(dumpResource(), hasItem(containsString("abc")));
    assertThat(dumpResource(), hasItem(containsString("ef")));
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
    partial = new RangeHeader(rangeHeader).apply(file);
  }

  private List<String> dumpResource() {
    ByteArrayOutputStream s = new ByteArrayOutputStream();
    try{
    partial.toResponse().write(s);
    } catch(IOException ex) {
    }
    return Arrays.asList(s.toString().split(HttpServer.CRLF));
  }

  private ResponseCode responseCode() {
    return partial.toResponse().getResponseCode();
  }
}

