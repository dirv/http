package com.danielirvine.http.contributors;

import java.util.List;

import com.danielirvine.http.InMemoryResourceCache;
import com.danielirvine.http.Request;
import com.danielirvine.http.RequestMethod;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.resources.FileResource;
import com.danielirvine.http.resources.Resource;
import com.danielirvine.http.responses.ErrorResponse;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class PatchResponseContributor implements ResponseContributor {

  private final InMemoryResourceCache cache;
  private final DirectoryResource root;
  private final List<String> writeablePaths;

  public PatchResponseContributor(DirectoryResource root,
      List<String> writeablePaths, InMemoryResourceCache cache) {
        this.root = root;
        this.writeablePaths = writeablePaths;
        this.cache = cache;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.getMethod().equals(RequestMethod.PATCH) && root.hasResource(request.getPathSegments());
  }

  @Override
  public Response respond(Request request) {
    
    if(!writeablePaths.contains(request.getPath())) {
      return new ErrorResponse(ResponseCode.METHOD_NOT_ALLOWED);
    }
    
    Resource file = root.findResource(request.getPathSegments());

    if(file.getETag().equals(request.getETag())) {
      file.write(request.getDataStream());
      cache.deleteContent(request.getPath());
      return new Response(ResponseCode.NO_CONTENT, ResponseHeader.EMPTY);
    }

    return new Response(ResponseCode.PRECONDITION_FAILED, ResponseHeader.EMPTY);
  }

}
