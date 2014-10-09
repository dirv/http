package com.danielirvine.http.headers.request;

import com.danielirvine.http.Request;

class RequestContentLengthHeader extends RequestHeader {
   public RequestContentLengthHeader(String value, Request request) {
     long length = Long.parseLong(value);
     request.setContentLength(length);
   }
}
