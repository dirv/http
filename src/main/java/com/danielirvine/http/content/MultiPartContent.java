package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.Arrays.*;

public class MultiPartContent extends ListContent {

  private final List<FixedRange> ranges;

  public MultiPartContent(List<Content> content, List<FixedRange> ranges) {
    super(content);
    this.ranges = ranges;
  }

  @Override
  public ContentTypeHeader contentType() {
    return ContentTypeHeader.MULTIPART_BYTE_RANGES;
  }

  @Override
  public List<ResponseHeader> additionalHeaders() {
    return asList();
  }

  @Override
  public long length() {
    // TODO: this is currently wrong for multi-parts as it won't
    // take into consideration the length of all range headers.
    return content.stream().mapToLong(c->c.length()).sum();
  }

  @Override
  public void write(OutputStream out) throws IOException {
      for(int i = 0; i < content.size(); ++i) {
        Content c = content.get(i);
        c.contentType().write(out);
        ranges.get(i).getHeader().write(out);
        out.write(HttpServer.CRLF.getBytes());
        c.write(out);
        // TODO - boundary
      }
    }
}