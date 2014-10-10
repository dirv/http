package com.danielirvine.http.ranges;

import com.danielirvine.http.headers.response.*;

public class FixedRange {

  private final long start;
  private final long end;
  private final long previousPosition;
  private final long totalLength;

  public FixedRange(long previousPosition, long start, long end, long totalLength) {
    this.previousPosition = previousPosition;
    this.start = start;
    this.end = end;
    this.totalLength = totalLength;
  }

  public long start() {
    return start;
  }

  public long end() {
    return end;
  }

  public long length() {
    return end - start + 1;
  }

  public long skip() {
    return start - previousPosition;
  }

  public boolean isSatisfiable() {
    return end >= start;
  }

  public ContentRangeHeader getHeader() {
    return new ContentRangeHeader(start, end, totalLength);
  }
}
