package com.danielirvine.http.contributors;

import com.danielirvine.http.*;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.responses.ErrorResponse;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class UnauthorizedResponseContributor implements ResponseContributor {

  private final Authorizer authorizer;

  public UnauthorizedResponseContributor(Authorizer authorizer) {
    this.authorizer = authorizer;
  }

  @Override
  public boolean canRespond(Request request) {
    String path = request.getPath();
    if (authorizer.requiresAuthorization(path)) {
      if(request.hasCredentials()) {
        String user = request.getUser();
        String password = request.getPassword();
        if(authorizer.isAuthorized(path, user, password)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public Response respond(Request request) {
    String realm = request.getPath();
    return new ErrorResponse(ResponseCode.UNAUTHORIZED,
        new ResponseHeader("WWW-Authenticate",
          "Basic realm=\"" + realm + "\""));
  }
}
