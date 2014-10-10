package com.danielirvine.http.content;

import java.util.*;

class RangedStreamer {
  private final FileDescriptor file;
  private final Queue<FixedRange> fixedRanges;
  private final BufferedReader in;

  public RangedStreamer(FileDesciptor file, List<FixedRange> ranges) {
    this.file = file;
    this.fixedRanges = new Queue<FixedRange>(ranges);
  }

  public void streamNext(PrintStream out) {
    if(!hasStarted()) {
      startStreaming(out);
    }
    streamRange(out, fixedRange.take());
    if(fixedRanges.empty()) {
      stopStreaming();
    }
  }

  public List<Content> toContent(List<FixedRange ranges) {
    return ranges.stream()
      .map(r->new RangedStreamerContent(this))
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
    in = new BufferedReader(new InputStreamReader(descriptor.getDataStream()));
  }

  private void stopStreaming() {
    in.close();
  }

  private class RangedStreamerContent implements Content {

    public void write(PrintStream out) {
      streamNext(out);
    }

    public long length() {
      return file.length();
    }

    public ContentTypeHeader contentType() {
      // TODO: this will get called repeatedly
      return new ContentTypeHeader(descriptor.contentType());
    }

    public List<Content> withRanges(List<FixedRange> ranges) {
      return toContent(ranges);
    }
  }
}
