package com.danielirvine.http.headers.response;

public class ContentTypeHeader extends ResponseHeader {

  public final static ContentTypeHeader MULTIPART_BYTE_RANGES = new ContentTypeHeader(
      "multipart/byteranges",
      "boundary=BREAK");

  public final static ContentTypeHeader TEXT_PLAIN = new ContentTypeHeader("text/plain");
  public final static ContentTypeHeader TEXT_HTML = new ContentTypeHeader("text/html");

  public ContentTypeHeader(String type, String... parameters) {
    super("Content-type", type, parameters);
  }
}
