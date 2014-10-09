package com.danielirvine.http.resources;

import java.io.*;
import java.util.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.content.*;
import com.danielirvine.http.ranges.FixedRange;
import com.danielirvine.http.ranges.Range;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

import static java.util.Arrays.*;

class PartialFileResource implements Resource {

  private final FileDescriptor descriptor;
  private final List<FixedRange> ranges;

  public PartialFileResource(FileDescriptor descriptor, List<Range> ranges) {
    this.descriptor = descriptor;
    this.ranges = fix(ranges, descriptor.length());
  }

  public Resource applyRange(List<Range> range) {
    return this;
  }

  public Response toResponse() {
    if(isSatisfiable()) {
    return new Response(ResponseCode.PARTIAL,
        new PartialHeadedContent(descriptor, ranges));
    } else {
      return new Response(ResponseCode.UNSATISFIABLE,
          new HtmlHeadedContent(
            new StringContent("Range request unsatisfiable")));
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

  private List<FixedRange> fix(List<Range> ranges, long fileLength) {
    long curPos = 0;

    List<FixedRange> specifiers = new ArrayList<FixedRange>();
    for(Range s : ranges) {
      FixedRange specifier = s.fix(curPos, fileLength);
      if(specifier.isSatisfiable()) {
        curPos += specifier.length() + 1;
        specifiers.add(specifier);
      }
    }
    return specifiers;
  }
}
