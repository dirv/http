package com.danielirvine.http.content;

import java.io.*;
import java.util.List;

import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

public class HeadedContent implements Content {
  
  private List<ResponseHeader> headers;
  private Content content;

  public HeadedContent(List<ResponseHeader> headers, Content content) {
    this.headers = headers;
    this.content = content;
  }

  @Override
  public void write(OutputStream out) throws IOException {
    content.write(out);
  }

  @Override
  public long length() {
    return content.length();
  }

  @Override
  public long lastModified() {
    return content.lastModified();
  }

  @Override
  public ContentTypeHeader contentType() {
    return content.contentType();
  }

  @Override
  public List<Content> withRanges(List<FixedRange> ranges) {
    return content.withRanges(ranges);
  }

  @Override
  public List<ResponseHeader> additionalHeaders() {
    return headers;
  }
}
