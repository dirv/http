package com.danielirvine.http.content;

import java.util.*;

import com.danielirvine.http.headers.response.*;

import static java.util.Arrays.*;

public class HtmlHeadedContent extends HeadedContent {

  public HtmlHeadedContent(List<Content> content) {
    super(generateHeaders(content), content);
  }

  private static List<ResponseHeader> generateHeaders(List<Content> content) {
    long length = content.stream().mapToLong(s->s.length()).sum();
    return asList(
        new ContentLengthHeader(length),
        ContentTypeHeader.TEXT_HTML);
  }
}
