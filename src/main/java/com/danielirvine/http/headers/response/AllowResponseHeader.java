package com.danielirvine.http.headers.response;

import java.util.List;

import com.danielirvine.http.RequestVerb;
import static java.util.stream.Collectors.*;

public class AllowResponseHeader extends ResponseHeader {

  public AllowResponseHeader(List<RequestVerb> allowedVerbs) {
    super("Allow", allowedVerbs.stream().map(v->v.toString()).collect(joining(",")));
  }
}
