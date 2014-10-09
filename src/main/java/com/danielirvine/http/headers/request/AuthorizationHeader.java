package com.danielirvine.http.headers.request;

import java.util.*;
import java.util.regex.*;

import com.danielirvine.http.Request;

class AuthorizationHeader extends RequestHeader {

  private static final Pattern basicPattern = Pattern.compile("Basic ([A-Za-z0-9=]+)");
  private static final Base64.Decoder decoder = Base64.getDecoder();

  public AuthorizationHeader(String header, Request request) {
    Matcher m = basicPattern.matcher(header);
    if (m.matches()) {
      String[] parts = decode(m.group(1)).split(":");
      request.setCredentials(parts[0], parts[1]);
    }
  }

  private String decode(String base64encoded) {
    try {
    return new String(decoder.decode(base64encoded), "UTF-8");
    } catch (Exception ex) {
      return null;
    }
  }
}
