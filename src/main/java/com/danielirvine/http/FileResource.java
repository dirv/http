package com.danielirvine.http;

import java.io.*;
import java.util.*;
import static java.util.Arrays.*;

class FileResource implements Resource {

  private final FileDescriptor descriptor;

  public FileResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public Resource applyRange(RangeHeader range) {
    return new PartialFileResource(descriptor, range);
  }

  public ResponseCode getResponseCode() {
    return ResponseCode.OK;
  }

  public void dumpResource(Writer out) {
    try {
      InputStream reader = descriptor.getReadStream();
      int b;
      while((b = reader.read()) != -1) {
        out.write(b);
      }
    } catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  public List<ResponseHeader> getHeaders() {
    List<ResponseHeader> headers = new ArrayList<ResponseHeader>();
    headers.add(new ContentTypeHeader(descriptor));
    headers.add(new ContentLengthHeader(descriptor));
    return headers;
  }
}
