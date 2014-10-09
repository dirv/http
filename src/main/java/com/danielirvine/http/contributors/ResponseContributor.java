package com.danielirvine.http.contributors;

import com.danielirvine.http.*;

public interface ResponseContributor {
  boolean canRespond(Request request);
  Response respond(Request request);
}
