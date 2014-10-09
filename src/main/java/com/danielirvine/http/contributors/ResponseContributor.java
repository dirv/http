package com.danielirvine.http.contributors;

import com.danielirvine.http.*;
import com.danielirvine.http.responses.Response;

public interface ResponseContributor {
  boolean canRespond(Request request);
  Response respond(Request request);
}
