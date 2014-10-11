package com.danielirvine.http.contributors;

import java.util.List;

import com.danielirvine.http.Request;
import com.danielirvine.http.RequestMethod;
import com.danielirvine.http.headers.response.AllowResponseHeader;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

import static java.util.Arrays.*;


public class OptionsResponseContributor implements ResponseContributor {

  private static final List<RequestMethod> READ_ONLY = asList(RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS);
  private static final List<RequestMethod> WRITEABLE = asList(RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT);
  private final List<String> writeablePaths;

  public OptionsResponseContributor(List<String> writeablePaths) {
    this.writeablePaths = writeablePaths;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.getMethod().equals(RequestMethod.OPTIONS);
  }

  @Override
  public Response respond(Request request) {
    List<RequestMethod> allowedVerbs;
    if(writeablePaths.contains(request.getPath())) {
      allowedVerbs = WRITEABLE;
    } else {
        allowedVerbs = READ_ONLY;
    }
    return new Response(ResponseCode.OK, asList(new AllowResponseHeader(allowedVerbs)));
  }
}
