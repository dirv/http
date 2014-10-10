package com.danielirvine.http.content;

import java.io.PrintStream;
import java.util.List;

public class ListContent implements Content {

  private List<Content> content;

  public ListContent(List<Content> content) {
    this.content = content;
  }

  public void write(PrintStream out) {
    for(Content c : content) {
      c.write(out);
    }
  }

  public long length() {
    return content.stream().mapToLong(s->s.length()).sum();
  }

  public ContentTypeHeader type() {
    return ContentTypeHeader.TEXT_HTML;
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    // TODO: need to split out ranges based on Content length, and
    // possibly fix ranges again.
    return new List<Content>();
  }

  public List<Header> additionalHeaders() {
    return new List<Header>();
  }
}
