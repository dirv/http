package com.danielirvine.http.ranges;


public class ByteRange implements Range {
  private final long low;
  private final Long high;

  public ByteRange(long low, Long high) {
    this.low = low;
    this.high = high;
  }

  public FixedRange fix(long previousPosition, long fileLength) {
    if (high == null || high > fileLength) {
      return new FixedRange(previousPosition, low, fileLength-1, fileLength);
    }
    return new FixedRange(previousPosition, low, high, fileLength);
  }
}