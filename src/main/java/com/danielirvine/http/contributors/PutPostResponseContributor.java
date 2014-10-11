package com.danielirvine.http.contributors;

import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.resources.Resource;
import com.danielirvine.http.responses.EmptyResponse;
import com.danielirvine.http.responses.ErrorResponse;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class PutPostResponseContributor implements ResponseContributor {

  private final DirectoryResource root;
  private final InMemoryResourceCache cache;
  private final List<String> writeablePaths;

  public PutPostResponseContributor(DirectoryResource root, List<String> writeablePaths, InMemoryResourceCache cache) {
    this.root = root;
    this.writeablePaths = writeablePaths;
    this.cache = cache;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.getVerb().equals(RequestVerb.PUT) || request.getVerb().equals(RequestVerb.POST);
  }

  @Override
  public Response respond(Request request) {
    if(!writeablePaths.contains(request.getPath())) {
      return new ErrorResponse(ResponseCode.METHOD_NOT_ALLOWED);
    }

    Resource child = root.findOrCreateResource(request.getPathSegments());
    child.write(request.getDataStream());
    cache.deleteContent(request.getPath());

    return new EmptyResponse(ResponseCode.OK);
  }
}
