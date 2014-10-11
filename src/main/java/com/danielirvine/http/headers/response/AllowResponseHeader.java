package com.danielirvine.http.headers.response;

import java.util.List;

import com.danielirvine.http.RequestMethod;
import static java.util.stream.Collectors.*;

public class AllowResponseHeader extends ResponseHeader {

  public AllowResponseHeader(List<RequestMethod> allowedVerbs) {
    super("Allow", allowedVerbs.stream().map(v->v.toString()).collect(joining(",")));
  }
}
