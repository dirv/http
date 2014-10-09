package com.danielirvine.http.resources;

import java.io.*;
import java.util.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.Response;
import com.danielirvine.http.ResponseCode;
import com.danielirvine.http.content.HeadedContent;
import com.danielirvine.http.content.SinglePartContent;
import com.danielirvine.http.headers.request.RangeHeader;
import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.Arrays.*;

public class FileResource implements Resource {

  private final FileDescriptor descriptor;

  public FileResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public Resource applyRange(RangeHeader range) {
    return new PartialFileResource(descriptor, range);
  }

  public Response toResponse() {
    return new Response(
        ResponseCode.OK,
        new HeadedContent(getHeaders(),
          asList(new SinglePartContent(descriptor, new FixedRange(descriptor)))));
  }

  @Override
  public void write(Reader in) {
    descriptor.write(in);
  }

  @Override
  public void delete() {
    descriptor.delete();
  }

  private List<ResponseHeader> getHeaders() {
    return asList(
        new ContentTypeHeader(descriptor),
        new ContentLengthHeader(descriptor));
  }
}
