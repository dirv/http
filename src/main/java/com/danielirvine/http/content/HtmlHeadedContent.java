package com.danielirvine.http.content;

import java.util.*;
import com.danielirvine.http.headers.response.*;
import static java.util.Arrays.*;

public class HtmlHeadedContent implements Content {

  private final Content content;

  public HtmlHeadedContent(Content content) {
    this.content = content;
  }

  public ContentType contentType() {
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

  public List<Header> additionalHeaders() {
    return new List<Header>();
  }
}
