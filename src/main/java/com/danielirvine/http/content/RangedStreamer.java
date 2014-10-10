package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.*;


import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

class RangedStreamer {
  private final FileDescriptor descriptor;
  private final Queue<FixedRange> fixedRanges;
  private final ContentTypeHeader contentTypeHeader;
  private BufferedReader in;

  public RangedStreamer(FileDescriptor descriptor, List<FixedRange> ranges) {
    this.descriptor = descriptor;
    this.fixedRanges = new LinkedList<FixedRange>(ranges);
    this.contentTypeHeader = new ContentTypeHeader(descriptor.contentType());
  }

  public void streamNext(OutputStream out) throws IOException {
    if(!hasStarted()) {
      startStreaming();
    }
    streamRange(out, fixedRanges.poll());
    if(fixedRanges.isEmpty()) {
      stopStreaming();
    }
  }

  public List<Content> toContent() {
    return fixedRanges.stream()
      .map(r->new RangedStreamerContent(r.length()))
      .collect(toList());
  }

  private void streamRange(OutputStream out, FixedRange range) throws IOException {
    in.skip(range.skip());
    int b;
    long length = range.length();
    long curPos = 0;
    while((b = in.read()) != -1 && ++curPos <= length) {
      out.write(b);
    }
  }

  private boolean hasStarted() {
    return in != null;
  }

  private void startStreaming() {
    in = new BufferedReader(new InputStreamReader(descriptor.getReadStream()));
  }

  private void stopStreaming() {
    try{
      in.close();
    } catch(IOException ex) {
    }
  }

  private class RangedStreamerContent implements Content {

    private final long length;

    public RangedStreamerContent(long length) {
      this.length = length;
    }

    public void write(OutputStream out) throws IOException {
      streamNext(out);
    }

    public long length() {
      return length;
    }

    public ContentTypeHeader contentType() {
      return contentTypeHeader;
    }

    public List<Content> withRanges(List<FixedRange> ranges) {
      return toContent();
    }

    public List<ResponseHeader> additionalHeaders() {
      return new ArrayList<ResponseHeader>();
    }
  }
}