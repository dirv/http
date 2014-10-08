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

  public Response toResponse() {
    // TODO: need to kill this stream somehow.
    // Introduce a Streamable interface.
    InputStream in = new BufferedInputStream(descriptor.getReadStream());
    return new Response(
        ResponseCode.OK,
        new HeadedContent(getHeaders(),
          asList(new StreamContent(0, descriptor.length(), in))));
  }

  private List<ResponseHeader> getHeaders() {
    return asList(
        new ContentTypeHeader(descriptor),
        new ContentLengthHeader(descriptor));
  }
}
