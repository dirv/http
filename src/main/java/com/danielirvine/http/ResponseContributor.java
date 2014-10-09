package com.danielirvine.http;

interface ResponseContributor {
  boolean canRespond(Request request);
  Response respond(Request request);
}
