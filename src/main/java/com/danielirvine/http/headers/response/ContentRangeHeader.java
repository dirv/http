package com.danielirvine.http.headers.response;

public class ContentRangeHeader extends ResponseHeader {

  public ContentRangeHeader(long low, long high, long fileLength) {
    super("Content-range", "bytes " + low + "-" + high + "/" + fileLength);
  }
}