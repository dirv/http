package com.danielirvine.http.headers.request;

import java.util.function.*;
import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.resources.Resource;

public class RequestHeader {

  private static final Map<String, BiFunction<String, Request, RequestHeader>> builders = new HashMap<String, BiFunction<String, Request, RequestHeader>>();

  private static final BiFunction<String, Request, RequestHeader> defaultBuilder;

  static {
    builders.put("Range", (v, r) -> new RangeHeader(v, r));
    builders.put("Authorization", (v, r) -> new AuthorizationHeader(v, r));
    builders.put("Content-Length", (v, r) -> new RequestContentLengthHeader(v, r));
    builders.put("If-Match", (v, r) -> new IfMatchHeader(v, r));
    defaultBuilder = (v, r) -> new UnknownRequestHeader();
  }

  public Resource apply(Resource resource) {
    return resource;
  }

  public static RequestHeader build(Request request, String headerString) {
    String[] parts = headerString.split(":");
    String fieldName = parts[0];
    String fieldValue = parts[1].trim();
    return findBuilder(fieldName).apply(fieldValue, request);
  }

  private static BiFunction<String, Request, RequestHeader> findBuilder(String fieldName) {
    if(builders.containsKey(fieldName)) {
      return builders.get(fieldName);
    }
    return defaultBuilder;
  }
}
