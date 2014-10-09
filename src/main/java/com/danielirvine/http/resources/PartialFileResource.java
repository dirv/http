package com.danielirvine.http.resources;

import java.io.*;
import java.util.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.Response;
import com.danielirvine.http.ResponseCode;
import com.danielirvine.http.content.*;
import com.danielirvine.http.headers.request.RangeHeader;
import com.danielirvine.http.ranges.FixedRange;

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
          new HtmlHeadedContent(
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
