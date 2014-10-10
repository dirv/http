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

  public MultiPartContent(List<RangedContent> partials) {
    super(partials);
  }

  public ContentTypeHeader contentType() {
    if(partials.size() > 1) {
      return ContentTypeHeader.MULTIPART_BYTE_RANGES;
    }
    return partials.get(0).contentType();
  }

  public List<Header> additionalHeaders() {
    if(partials.size() == 1) {
      return asList(partials.get(0).getRange().getHeader());
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
      for(RangedContent c : content) {
        out.print(c.contentType());
        out.print(c.getRange().getHeader());
        out.print(HttpServer.CRLF);
        c.write(out);
        // TODO - boundary
      }
    }
  }
