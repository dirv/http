package com.danielirvine.http;

import java.util.*;
import static java.util.Arrays.*;

class PartialHeadedContent extends HeadedContent {

  public PartialHeadedContent(FileDescriptor descriptor, List<FixedRange> ranges) {
    super(getHeaders(descriptor, ranges), asList(getContent(descriptor, ranges)));
  }

  private static Content getContent(FileDescriptor descriptor, List<FixedRange> ranges) {
    if (ranges.size() > 1) {
      return new MultiPartContent(descriptor, ranges);
    } else {
      return new SinglePartContent(descriptor, ranges.get(0));
    }
  }

  private static List<ResponseHeader> getHeaders(FileDescriptor descriptor, List<FixedRange> ranges) {
    if (ranges.size() == 1) {
      FixedRange range = ranges.get(0);
      return asList(
          new ContentTypeHeader(descriptor),
          new ContentLengthHeader(range.length()),
          range.getHeader());
    } else {
      return asList(ContentTypeHeader.MULTIPART_BYTE_RANGES);
    }
  }
}
