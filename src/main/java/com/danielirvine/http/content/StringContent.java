package com.danielirvine.http.content;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

public class StringContent implements Content {

  private final String content;

  public StringContent(String content) {
    this.content = content;
  }

  public long length() {
    return content.length();
  }

  public List<ResponseHeader> additionalHeaders() {
    return new ArrayList<ResponseHeader>();
  }

  public void write(PrintStream out) {
    out.print(content);
  }

  public ContentTypeHeader contentType() {
    return ContentTypeHeader.TEXT_PLAIN;
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    // TODO
    //FixedRange range = ranges.get(0);
    return new ArrayList<Content>();
    //return asList(new StringContent(content.substring(range.getLow(), range.length())));
  }
}
