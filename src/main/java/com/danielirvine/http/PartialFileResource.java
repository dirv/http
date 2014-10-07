package com.danielirvine.http;

import java.io.*;
import static java.util.stream.Stream.*;
import java.util.stream.*;
import java.util.*;
import static java.util.Arrays.*;

public class PartialFileResource implements Resource {

  private final FileDescriptor descriptor;
  private final List<FixedRangeSpecifier> ranges;

  public PartialFileResource(FileDescriptor descriptor, RangeHeader range) {
    this.descriptor = descriptor;
    this.ranges = range.fixForFileLength(descriptor.length());
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }

  public void dumpResource(PrintWriter out) {
    if(isSatisfiable()) {
      InputStream reader = descriptor.getReadStream();
      long curPos = 0;
      try {
        for(FixedRangeSpecifier range : ranges) {
          addHeader(out, ContentTypeHeader.TEXT_PLAIN);
          addHeader(out, range.getContentRangeHeader());
          reader.skip(range.getLow() - curPos);
          long high = range.getHigh();
          int b;
          while((b = reader.read()) != -1 && curPos++ <= high) {
            out.write(b);
          }
        }
      } catch(IOException ex) {
        System.err.println(ex);
      }
    }
  }

  public ResponseCode getResponseCode() {
    return isSatisfiable() ? ResponseCode.PARTIAL : ResponseCode.UNSATISFIABLE;
  }

  public List<Header> getHeaders() {
    return isSatisfiable()
      ? asList(ContentTypeHeader.MULTIPART_BYTE_RANGES)
      : asList();
  }

  private void addHeader(PrintWriter out, Header h) {
    out.write(h.toString() + HttpServer.CRLF);
  }
  private boolean isSatisfiable() {
    return ranges.size() > 0;
  }
}
