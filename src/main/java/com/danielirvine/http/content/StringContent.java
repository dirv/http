package com.danielirvine.http.content;

import java.io.*;

public class StringContent implements Content {

  private final String content;

  public StringContent(String content) {
    this.content = content;
  }

  public long length() {
    return content.length();
  }

  public List<Header> additionalHeaders() {
    return new List<Header>();
  }

  public void write(PrintStream out) {
    out.print(content);
  }

  public ContentTypeHeader type() {
    return ContentTypeHeader.TEXT_PLAIN;
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    // TODO
    FixedRange range = ranges.get(0);
    return asList(new StringContent(content.substring(range.getLow(), range.getLength())));
  }
}
