package com.danielirvine.http;

interface ResponseContributor {
  boolean canRespond(Request request);
  Response response(Request request);
}
