package com.danielirvine.http;

import java.io.*;

class FixedRange {

  private final long start;
  private final long end;
  private final long previousPosition;
  private final long totalLength;

  public FixedRange(FileDescriptor descriptor) {
    this(0, 0, descriptor.length(), descriptor.length());
  }

  public FixedRange(long previousPosition, long start, long end, long totalLength) {
    this.previousPosition = previousPosition;
    this.start = start;
    this.end = end;
    this.totalLength = totalLength;
  }

  public long length() {
    return end - start + 1;
  }

  private long skip() {
    return start - previousPosition;
  }

  public boolean isSatisfiable() {
    return end >= start;
  }

  public ContentRangeHeader getHeader() {
    return new ContentRangeHeader(start, end, totalLength);
  }

  public StreamContent toContent(InputStream in) {
    return new StreamContent(skip(), length(), in);
  }
}
