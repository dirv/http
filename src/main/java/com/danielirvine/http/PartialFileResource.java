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
          if(isMultipart()) {
            addHeader(out, new ContentTypeHeader(descriptor));
            addHeader(out, range.getContentRangeHeader());
          }
          reader.skip(range.getLow() - curPos);
          long high = range.getHigh();
          int b;
          while((b = reader.read()) != -1 && curPos++ <= high) {
            out.write(b);
          }
        }
      } catch(IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  public ResponseCode getResponseCode() {
    return isSatisfiable() ? ResponseCode.PARTIAL : ResponseCode.UNSATISFIABLE;
  }

  public List<Header> getHeaders() {
    List<Header> headers = new ArrayList<Header>();
    if(isMultipart()) {
      headers.add(ContentTypeHeader.MULTIPART_BYTE_RANGES);
    } else {
      headers.add(new ContentTypeHeader(descriptor));
      headers.add(ranges.get(0).getContentRangeHeader());
      headers.add(getContentLengthHeader());
    }
    return headers;
  }

  private void addHeader(PrintWriter out, Header h) {
    out.write(h.toString() + HttpServer.CRLF);
  }

  private boolean isMultipart() {
    return isSatisfiable() && ranges.size() > 1;
  }

  private boolean isSatisfiable() {
    return ranges.size() > 0;
  }

  private Header getContentLengthHeader() {
    return new Header() {
      @Override
      public String toString() {
        long length = isSatisfiable() ? ranges.get(0).length() : descriptor.length();
        return "Content-Length: " + length;
      }
    };
  }
}
