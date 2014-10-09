package com.danielirvine.http;

class RequestContentLengthHeader extends RequestHeader {
  
  
   public RequestContentLengthHeader(String value, Request request) {
     long length = Long.parseLong(value);
     request.setContentLength(length);
   }
}
