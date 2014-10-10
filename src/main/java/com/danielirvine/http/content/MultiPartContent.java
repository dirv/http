package com.danielirvine.http.content;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import com.danielirvine.http.*;
import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.Arrays.*;

class MultiPartContent extends ListContent {

  private final List<FixedRange> ranges;

  public MultiPartContent(List<FixedRange> ranges, List<Content> partials) {
    super(partials);
  }

  public ContentTypeHeader contentType() {
    if(partials.size() == 1) {
      return partials.get(0).contentType();
    }
    return ContentTypeHeader.MULTIPART_BYTE_RANGES;
  }

  public List<Header> additionalHeaders() {
    if(partials.size() == 1) {
      return asList(ranges.get(0).getHeader());
    }
    return asList();
  }

  public long length() {
    return 0; // TODO- this needs to be fixed.
  }

  public void write(PrintStream out) {
    if(partials.size() == 1) {
      partials.get(0).write(out);
    } else {
      for(int i = 0; i < partials.length; ++i) {
        Content c = partials.get(i);
        FixedRange r = ranges.get(i);
        out.print(c.contentType());
        out.print(r.getHeader());
        out.print(HttpServer.CRLF);
        c.write(out);
        // TODO - boundary

      }
    }
  }
