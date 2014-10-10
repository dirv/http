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
  }

  public void write(PrintStream out) {
    out.print(content);
  }

  public ContentTypeHeader type() {
    return ContentTypeHeader.TEXT_PLAIN;
  }

  public Content withRange(Range range) {
    return new StringContent(content.substring(range.getLow(), range.getLength()));
  }
}
