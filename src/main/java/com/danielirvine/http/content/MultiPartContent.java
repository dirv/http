package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.Arrays.*;

public class MultiPartContent extends ListContent {

  private final List<FixedRange> ranges;

  public MultiPartContent(Content content, List<FixedRange> ranges) {
    super(content.withRanges(ranges));
    this.ranges = ranges;
  }

  @Override
  public ContentTypeHeader contentType() {
    if(content.size() == 1) {
      return content.get(0).contentType();
    }
    return ContentTypeHeader.MULTIPART_BYTE_RANGES;
  }

  @Override
  public List<ResponseHeader> additionalHeaders() {
    if(content.size() == 1) {
      return asList(ranges.get(0).getHeader());
    }
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
    if(content.size() == 1) {
      content.get(0).write(out);
    } else {
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
}