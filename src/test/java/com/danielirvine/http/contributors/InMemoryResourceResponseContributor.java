package com.danielirvine.http.contributors;

import com.danielirvine.http.InMemoryResourceCache;
import com.danielirvine.http.Request;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class InMemoryResourceResponseContributor implements ResponseContributor {

  private final InMemoryResourceCache cache;

  public InMemoryResourceResponseContributor(InMemoryResourceCache cache) {
    this.cache = cache;
  }

  @Override
  public boolean canRespond(Request request) {
    return cache.hasContent(request.getPath());
  }

  @Override
  public Response respond(Request request) {
    return new Response(ResponseCode.OK, cache.getContent(request.getPath()));
  }

}
