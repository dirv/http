package com.danielirvine.http;

import java.util.*;

public class UrlRedirects {

  private final Map<String,String> redirects = new HashMap<String, String>();

  public UrlRedirects(List<String> urlRedirects) {
    for(String urlRedirect : urlRedirects) {
      parseRedirect(urlRedirect);
    }
  }

  private void parseRedirect(String line) {
    String[] parts = line.split("=>");
    redirects.put(parts[0].trim(), parts[1].trim());
  }

  public boolean hasRedirect(String url) {
    return redirects.containsKey(url);
  }

  public String redirect(String url) {
    return redirects.get(url);
  }
}
