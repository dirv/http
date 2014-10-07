package com.danielirvine.http;

public interface FixedRangeSpecifier {
  public long getLow();
  public long getHigh();
  public boolean isSatisfiable();
  public ResponseHeader toHeader();
  public long length();
}
