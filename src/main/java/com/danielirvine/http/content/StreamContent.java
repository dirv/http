package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

public class StreamContent implements Content {

  private final FileDescriptor descriptor;

  public StreamContent(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public void write(OutputStream out) throws IOException {
    try(BufferedInputStream in = new BufferedInputStream(descriptor.getReadStream())) {
      int b;
      while((b = in.read()) != -1) {
        out.write(b);
      }
    }
  }

  public long length() {
    return descriptor.length();
  }

  public ContentTypeHeader contentType() {
    return new ContentTypeHeader(descriptor.contentType());
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    return new RangedStreamer(descriptor, ranges).toContent();
  }

  public List<ResponseHeader> additionalHeaders() {
    return new ArrayList<ResponseHeader>();
  }
}
