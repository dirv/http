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
  private BufferedReader in;

  public RangedStreamer(FileDescriptor descriptor, List<FixedRange> ranges) {
    this.descriptor = descriptor;
    this.fixedRanges = new LinkedList<FixedRange>(ranges);
  }

  public void streamNext(PrintStream out) {
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
      .map(r->new RangedStreamerContent())
      .collect(toList());
  }

  private void streamRange(PrintStream out, FixedRange range) {
    try {
      in.skip(range.skip());
      int b;
      long length = range.length();
      long curPos = 0;
      while((b = in.read()) != -1 && ++curPos <= length) {
        out.write(b);
      }
    }
    catch(IOException ex) {
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

    public void write(PrintStream out) {
      streamNext(out);
    }

    public long length() {
      return descriptor.length();
    }

    public ContentTypeHeader contentType() {
      // TODO: this will get called repeatedly
      return new ContentTypeHeader(descriptor.contentType());
    }

    public List<Content> withRanges(List<FixedRange> ranges) {
      return toContent();
    }

    @Override
    public List<ResponseHeader> additionalHeaders() {
      return new ArrayList<ResponseHeader>();
    }
  }
}
