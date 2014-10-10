package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.Arrays.*;

public class MultiPartContent extends ListContent {

  private final List<FixedRange> ranges;

  public MultiPartContent(List<FixedRange> ranges, List<Content> content) {
    super(content);
    this.ranges = ranges;
  }

  public ContentTypeHeader contentType() {
    if(content.size() == 1) {
      return content.get(0).contentType();
    }
    return ContentTypeHeader.MULTIPART_BYTE_RANGES;
  }

  public List<ResponseHeader> additionalHeaders() {
    if(content.size() == 1) {
      return asList(ranges.get(0).getHeader());
    }
    return asList();
  }

  public long length() {
    return 0; // TODO- this needs to be fixed.
  }

  public void write(PrintStream out) {
    if(content.size() == 1) {
      content.get(0).write(out);
    } else {
      for(int i = 0; i < content.size(); ++i) {
        Content c = content.get(i);
        FixedRange r = ranges.get(i);
        out.print(c.contentType());
        out.print(r.getHeader());
        out.print(HttpServer.CRLF);
        c.write(out);
        // TODO - boundary

      }
    }
  }
}