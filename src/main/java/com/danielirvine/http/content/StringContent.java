package com.danielirvine.http.content;

import com.danielirvine.http.headers.response.ContentTypeHeader;

public class StringContent extends ByteArrayContent {
  public StringContent(String content) {
    super(content.getBytes(), ContentTypeHeader.TEXT_PLAIN);
  }
}
