package com.danielirvine.http;

import java.util.*;

public class ResponseHeader {

  private final String header;
  private final String value;
  private final String[] parameters;

  public ResponseHeader(String header, String value, String... parameters) {
    this.header = header;
    this.value = value;
    this.parameters = parameters;
  }

  @Override
  public String toString() {
    String str = header + ": " + value;
    for(String parameter : parameters) {
      str += "; " + parameter;
    }
    return str;
  }
}

class ContentTypeHeader extends ResponseHeader {

  public final static ContentTypeHeader MULTIPART_BYTE_RANGES = new ContentTypeHeader(
      "multipart/byteranges",
      "boundary=BREAK");

  public final static ContentTypeHeader TEXT_PLAIN = new ContentTypeHeader("text/plain");

  public ContentTypeHeader(FileDescriptor descriptor) {
    this(descriptor.contentType());
  }

  public ContentTypeHeader(String type, String... parameters) {
    super("Content-type", type, parameters);
  }
}

class ContentLengthHeader extends ResponseHeader {

  public ContentLengthHeader(FileDescriptor descriptor) {
    this(descriptor.length());
  }

  public ContentLengthHeader(long length) {
    super("Content-Length", String.valueOf(length));
  }
}

class ContentRangeHeader extends ResponseHeader {

  public ContentRangeHeader(long low, long high, long fileLength) {
    super("Content-range", "bytes " + low + "-" + high + "/" + fileLength);
  }
}
