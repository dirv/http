package com.danielirvine.http.headers.request;

import com.danielirvine.http.Request;

public class IfMatchHeader extends RequestHeader {
  
  public IfMatchHeader(String header, Request request) {
    request.setETag(header.trim());
  }

}
