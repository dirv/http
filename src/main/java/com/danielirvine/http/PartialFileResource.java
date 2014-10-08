package com.danielirvine.http;

import java.io.*;
import static java.util.stream.Stream.*;
import java.util.stream.*;
import java.util.*;
import static java.util.Arrays.*;

public class PartialFileResource implements Resource {

  private final FileDescriptor descriptor;
  private final List<FixedRangeSpecifier> ranges;

  public PartialFileResource(FileDescriptor descriptor, RangeHeader range) {
    this.descriptor = descriptor;
    this.ranges = range.fixForFileLength(descriptor.length());
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }

  public Response toResponse() {
    if(isSatisfiable()) {
    return new Response(ResponseCode.PARTIAL,
        getContent());
    } else {
      return new Response(ResponseCode.UNSATISFIABLE,
          new PlainTextHeadedContent(
            asList(new StringContent("Range request unsatisfiable"))));
    }
  }

  private HeadedContent getContent() {
    if(isMultipart()) {
      return new HeadedContent(
          asList(ContentTypeHeader.MULTIPART_BYTE_RANGES),
          asList(new PartialHeadedContent(descriptor, ranges)));
    } else {
      // TODO: closing of the stream
      InputStream in = new BufferedInputStream(descriptor.getReadStream());
      FixedRangeSpecifier range = ranges.get(0);
      return new HeadedContent(
          getHeaders(range),
          asList(new StreamContent(range.getLow(), range.length(), in)));
    }
  }

  public List<ResponseHeader> getHeaders(FixedRangeSpecifier range) {
    List<ResponseHeader> headers = new ArrayList<ResponseHeader>();
    headers.add(new ContentTypeHeader(descriptor));
    headers.add(range.toHeader());
    headers.add(new ContentLengthHeader(range.length()));
    return headers;
  }

  private boolean isMultipart() {
    return isSatisfiable() && ranges.size() > 1;
  }

  private boolean isSatisfiable() {
    return ranges.size() > 0;
  }
}
