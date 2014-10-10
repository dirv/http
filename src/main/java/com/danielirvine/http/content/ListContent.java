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
    // TODO
    // 1. If it's a single range, then retunr a new HeadedContent with a new Header.
    // 2. If it's multiple range, return a new HeadedContent with mutliple HeadedContents within it.
    new HeadedContent(
  }
}
