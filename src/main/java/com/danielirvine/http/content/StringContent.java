package com.danielirvine.http.content;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;
import static java.util.stream.Collectors.*;

public class StringContent implements Content {

  private final String content;

  public StringContent(String content) {
    this.content = content;
  }

  public long length() {
    return content.length();
  }

  public List<ResponseHeader> additionalHeaders() {
    return new ArrayList<ResponseHeader>();
  }

  public void write(OutputStream out) throws IOException {
    out.write(content.getBytes());
  }

  public ContentTypeHeader contentType() {
    return ContentTypeHeader.TEXT_PLAIN;
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    return ranges.stream()
        .map(r->new StringContent(content.substring((int)r.start(), (int)r.length())))
        .collect(toList());
  }
}
