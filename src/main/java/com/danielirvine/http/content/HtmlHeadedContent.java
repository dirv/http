package com.danielirvine.http.content;

import java.util.*;

import com.danielirvine.http.headers.response.*;

import static java.util.Arrays.*;

public class HtmlHeadedContent extends HeadedContent {

  public HtmlHeadedContent(Content content) {
    super(generateHeaders(content), content);
  }

  private static List<ResponseHeader> generateHeaders(Content content) {
    return asList(
        new ContentLengthHeader(content.length()),
        ContentTypeHeader.TEXT_HTML);
  }
}
