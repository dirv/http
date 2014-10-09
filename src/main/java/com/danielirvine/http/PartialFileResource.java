package com.danielirvine.http;

import java.io.*;
import java.util.*;

import static java.util.Arrays.*;

class PartialFileResource implements Resource {

  private final FileDescriptor descriptor;
  private final List<FixedRange> ranges;

  public PartialFileResource(FileDescriptor descriptor, RangeHeader range) {
    this.descriptor = descriptor;
    this.ranges = range.fix(descriptor.length());
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }

  public Response toResponse() {
    if(isSatisfiable()) {
    return new Response(ResponseCode.PARTIAL,
        new PartialHeadedContent(descriptor, ranges));
    } else {
      return new Response(ResponseCode.UNSATISFIABLE,
          new PlainTextHeadedContent(
            asList(new StringContent("Range request unsatisfiable"))));
    }
  }

  @Override
  public void write(Reader in) {
  }


  @Override
  public void delete() {
  }
  
  private boolean isSatisfiable() {
    return ranges.size() > 0;
  }
}
