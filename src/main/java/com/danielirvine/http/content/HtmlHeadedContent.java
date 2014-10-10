package com.danielirvine.http.content;

import java.io.PrintStream;
import java.util.*;

import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

public class HtmlHeadedContent implements Content {

  private final Content content;

  public HtmlHeadedContent(Content content) {
    this.content = content;
  }

  public ContentTypeHeader contentType() {
    return ContentTypeHeader.TEXT_HTML;
  }

  public long length() {
    return content.length();
  }

  public void write(PrintStream out) {
    content.write(out);
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    return content.withRanges(ranges); // TODO: needs to apply a content type of text_html
  }

  public List<ResponseHeader> additionalHeaders() {
    return new ArrayList<ResponseHeader>();
  }
}
