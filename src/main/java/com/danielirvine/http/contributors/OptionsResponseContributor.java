package com.danielirvine.http.contributors;

import java.util.List;

import com.danielirvine.http.Request;
import com.danielirvine.http.RequestVerb;
import com.danielirvine.http.headers.response.AllowResponseHeader;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

import static java.util.Arrays.*;


public class OptionsResponseContributor implements ResponseContributor {

  private static final List<RequestVerb> READ_ONLY = asList(RequestVerb.GET, RequestVerb.HEAD, RequestVerb.OPTIONS);
  private static final List<RequestVerb> WRITEABLE = asList(RequestVerb.GET, RequestVerb.HEAD, RequestVerb.POST, RequestVerb.OPTIONS, RequestVerb.PUT);
  private final List<String> writeablePaths;

  public OptionsResponseContributor(List<String> writeablePaths) {
    this.writeablePaths = writeablePaths;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.getVerb().equals(RequestVerb.OPTIONS);
  }

  @Override
  public Response respond(Request request) {
    List<RequestVerb> allowedVerbs;
    if(writeablePaths.contains(request.getPath())) {
      allowedVerbs = WRITEABLE;
    } else {
        allowedVerbs = READ_ONLY;
    }
    return new Response(ResponseCode.OK, asList(new AllowResponseHeader(allowedVerbs)));
  }
}
