package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

public class ListContent implements Content {

  protected List<Content> content;
  private final long lastModified;

  public ListContent(List<Content> content) {
    this.content = content;
    this.lastModified = calculateLastModified(content).orElse(System.currentTimeMillis());
  }

  public void write(OutputStream out) throws IOException {
    for(Content c : content) {
      c.write(out);
    }
  }

  public long length() {
    return content.stream().mapToLong(s->s.length()).sum();
  }

  public ContentTypeHeader contentType() {
    return ContentTypeHeader.TEXT_HTML;
  }

  @Override
  public long lastModified() {
    return lastModified;
  }

  private static OptionalLong calculateLastModified(List<Content> content) {
    return content.stream().mapToLong(s->s.lastModified()).max();
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    // TODO: need to split out ranges based on Content length, and
    // possibly fix ranges again.
    return new ArrayList<Content>();
  }

  public List<ResponseHeader> additionalHeaders() {
    return ResponseHeader.EMPTY;
  }
}
