package com.danielirvine.http.ranges;


public class SuffixRange implements Range {
  private final long length;

  public SuffixRange(long length) {
    this.length = length;
  }

  public FixedRange fix(long previousPosition, long fileLength) {
    long low = fileLength - length;
    if (low < 0) low = 0;
    return new FixedRange(previousPosition, low, fileLength-1, fileLength);
  }
}