package com.danielirvine.http;

import java.util.*;
import java.util.regex.*;

class AuthorizationHeader implements RequestHeader {

  private static final Pattern basicPattern = Pattern.compile("Basic ([A-Za-z0-9=]+)");
  private static final Base64.Decoder decoder = Base64.getDecoder();

  public AuthorizationHeader(String header, Request request) {
    Matcher m = basicPattern.matcher(header);
    if (m.matches()) {
      String[] parts = decode(m.group(1)).split(":");
      request.setCredentials(parts[0], parts[1]);
    }
  }

  @Override
  public Resource apply(Resource resource) {
    return resource;
  }

  private String decode(String base64encoded) {
    try {
    return new String(decoder.decode(base64encoded), "UTF-8");
    } catch (Exception ex) {
      return null;
    }
  }
}
