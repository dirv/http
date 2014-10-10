package com.danielirvine.http.content;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.stream.Collectors.*;

public class ByteArrayContent implements Content {

  private static final List<ResponseHeader> HEADERS = new ArrayList<ResponseHeader>();
  private final byte[] content;
  private final ContentTypeHeader contentTypeHeader;

  public ByteArrayContent(byte[] content, ContentTypeHeader contentTypeHeader) {
    this.content = content;
    this.contentTypeHeader = contentTypeHeader;
  }

  public long length() {
    return content.length;
  }

  public List<ResponseHeader> additionalHeaders() {
    return HEADERS;
  }

  public void write(OutputStream out) throws IOException {
    out.write(content);
  }

  public ContentTypeHeader contentType() {
    return contentTypeHeader;
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    return ranges.stream()
        .map(r->new ByteArrayContent(Arrays.copyOfRange(content, (int)r.start(), (int)r.end()), contentTypeHeader))
        .collect(toList());
  }
}
