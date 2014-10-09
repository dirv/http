package com.danielirvine.http.content;


public class LinkContent extends StringContent {
  public LinkContent(String name, String url) {
    super("<a href=\"/" + url + "\">" + name + "</a><br></br>");
  }
}
