package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.*;

public class RangeHeaderTest {

  private static final String content = "abcdefghijklmnopqrstuvwxyz";
  private final Range range = new Range();
  private final FileResource file = new FileResource(new InMemoryFileDescriptor("test", content));
  private Resource partial;

  @Test
  public void rangeOfFirstBytes() {
    setRangeOnFile("bytes=0-2");
    assertEquals("abc", dumpResource());
  }

  @Test
	public void invalidRange() {
    setRangeOnFile("bytes=2-0");
    assertEquals(content, dumpResource());
    assertEquals(ResponseCode.OK, responseCode());
  }

  @Test
	public void multipleRanges() {
    setRangeOnFile("bytes=0-2,4-5");
    assertEquals("abcef", dumpResource());
  }

  @Test
	public void openRange() {
    setRangeOnFile("bytes=20-");
    assertEquals("uvwxyz", dumpResource());
  }

  @Test
  public void overruningRange() {
    setRangeOnFile("bytes=20-30");
    assertEquals("uvwxyz", dumpResource());
  }

  @Test
  public void gibberishRange() {
    setRangeOnFile("bytes=2030");
    assertEquals(content, dumpResource());
    assertEquals(ResponseCode.OK, responseCode());
  }

  @Test
	public void suffixRange() {
    setRangeOnFile("bytes=-5");
    assertEquals("vwxyz", dumpResource());
    assertEquals(ResponseCode.PARTIAL, responseCode());
  }

  @Test
	public void overrunningSuffixRange() {
    setRangeOnFile("bytes=-50");
    assertEquals(content, dumpResource());
  }

  @Test
	public void zeroSuffixRange() {
    setRangeOnFile("bytes=-0");
    assertEquals("", dumpResource());
    assertEquals(ResponseCode.UNSATISFIABLE, responseCode());
  }

  @Test
	public void overLengthRange() {
    setRangeOnFile("bytes=30-40");
    assertEquals("", dumpResource());
    assertEquals(ResponseCode.UNSATISFIABLE, responseCode());
  }


  private void setRangeOnFile(String rangeHeader) {
    Range range = new Range();
    range.processHeader(rangeHeader);
    partial = file.applyRange(range);
  }

  private String dumpResource() {
    StringWriter sw = new StringWriter();
    PrintWriter out = new PrintWriter(sw);
    partial.dumpResource(out);
    return sw.toString();
  }

  private ResponseCode responseCode() {
    return partial.getResponseCode();
  }
}

