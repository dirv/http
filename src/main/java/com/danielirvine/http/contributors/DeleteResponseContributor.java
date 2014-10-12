package com.danielirvine.http.contributors;

import java.util.List;

import com.danielirvine.http.*;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.responses.EmptyResponse;
import com.danielirvine.http.responses.ErrorResponse;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class DeleteResponseContributor implements ResponseContributor {

  private final DirectoryResource root;
  private final List<String> writeablePaths;

  public DeleteResponseContributor(DirectoryResource root, List<String> writeablePaths) {
    this.root = root;
    this.writeablePaths = writeablePaths;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.getMethod().equals(RequestMethod.DELETE) && root.hasResource(request.getPathSegments());
  }

  @Override
  public Response respond(Request request) {
    if(!writeablePaths.contains(request.getPath())){
      // TODO - add in allow header
      return new ErrorResponse(ResponseCode.METHOD_NOT_ALLOWED);
    }
    root.findResource(request.getPathSegments()).delete();
    return new EmptyResponse(ResponseCode.OK);
  }

}
