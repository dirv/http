package com.danielirvine.http;

public class LinkContent extends StringContent {
  public LinkContent(String name, String url) {
    super("<a href=\"/" + url + "\">" + name + "</a><br></br>");
  }
}
