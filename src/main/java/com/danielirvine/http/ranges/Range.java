package com.danielirvine.http.ranges;


public interface Range {
  public FixedRange fix(long previousPosition, long fileLength);
}