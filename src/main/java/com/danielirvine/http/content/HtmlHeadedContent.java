package com.danielirvine.http.content;

import com.danielirvine.http.headers.response.*;

public class HtmlHeadedContent extends HeadedContent {

  public HtmlHeadedContent(Content content) {
    super(ResponseHeader.EMPTY, content);
  }

  @Override
  public ContentTypeHeader contentType() {
    return ContentTypeHeader.TEXT_HTML;
  }
}
