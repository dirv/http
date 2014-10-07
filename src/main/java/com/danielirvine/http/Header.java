package com.danielirvine.http;

import java.util.*;

public interface Header {

}

class ContentTypeHeader implements Header {

  private final String type;
  private final String subtype;
  private final String[] parameters;

  public final static ContentTypeHeader MULTIPART_BYTE_RANGES = new ContentTypeHeader(
      "multipart",
      "byteranges",
      "boundary=BREAK");

  public final static ContentTypeHeader TEXT_PLAIN = new ContentTypeHeader("text", "plain");

  public ContentTypeHeader(String type, String subtype, String... parameters) {
    this.type = type;
    this.subtype = subtype;
    this.parameters = parameters;
  }

  @Override
  public String toString() {
    String str = "Content-type: " + type + "/" + subtype;
    for(String parameter : parameters) {
      str += "; " + parameter;
    }
    return str;
  }
}
