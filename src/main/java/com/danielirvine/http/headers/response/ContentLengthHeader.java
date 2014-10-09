package com.danielirvine.http.headers.response;

import com.danielirvine.http.FileDescriptor;

public class ContentLengthHeader extends ResponseHeader {

  public ContentLengthHeader(FileDescriptor descriptor) {
    this(descriptor.length());
  }

  public ContentLengthHeader(long length) {
    super("Content-Length", String.valueOf(length));
  }
}