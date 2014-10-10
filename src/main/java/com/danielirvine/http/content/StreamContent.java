package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

public class StreamContent implements Content {

  private final FileDescriptor descriptor;

  public StreamContent(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public void write(PrintStream out) {
    try {
      in.skip(skip);
      int b;
      long curPos = 0;
      while((b = in.read()) != -1 && ++curPos <= length) {
        out.write(b);
      }
    }
    catch(IOException ex) {
    }
  }

  public long length() {
    return descriptor.length();
  }

  public ContentTypeHeader contentType() {
    return new ContentTypeHeader(descriptor.contentType());
  }

  public List<Content> withRanges(List<FixedRange> ranges) {
    return new RangedStreamer(file, ranges).toContent();
  }
}
