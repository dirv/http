package com.danielirvine.http.content;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.stream.Collectors.*;

public class ByteArrayContent implements Content {

  private final byte[] content;
  private final ContentTypeHeader contentTypeHeader;
  private final long lastModified;

  public ByteArrayContent(byte[] content, ContentTypeHeader contentTypeHeader, long lastModified) {
    this.content = content;
    this.contentTypeHeader = contentTypeHeader;
    this.lastModified = lastModified;
  }

  public long length() {
    return content.length;
  }

  public List<ResponseHeader> additionalHeaders() {
    return ResponseHeader.EMPTY;
  }

  public void write(OutputStream out) throws IOException {
    out.write(content);
  }

  public ContentTypeHeader contentType() {
    return contentTypeHeader;
  }

  public long lastModified() {
    return lastModified;
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    return ranges.stream()
        .map(r->new ByteArrayContent(Arrays.copyOfRange(content, (int)r.start(), (int)r.end()), contentTypeHeader, lastModified))
        .collect(toList());
  }
  
  public static ByteArrayContent convert(Content content) throws IOException {
    try(ByteArrayOutputStream str = new ByteArrayOutputStream()) {
      try(BufferedOutputStream out = new BufferedOutputStream(str)) {
        content.write(out);
        out.flush();
      }
      return new ByteArrayContent(str.toByteArray(), content.contentType(), content.lastModified());
    }
  }
}
