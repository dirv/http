package com.danielirvine.http.content;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import com.danielirvine.http.*;
import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.headers.response.*;
import com.danielirvine.http.ranges.FixedRange;

import static java.util.Arrays.*;

class MultiPartContent implements Content {
  private final FileDescriptor descriptor;
  private final List<FixedRange> ranges;

  public MultiPartContent(FileDescriptor descriptor, List<FixedRange> ranges) {
    this.descriptor = descriptor;
    this.ranges = ranges;
  }

  public long length() {
    return descriptor.length(); // TODO - should possibly be total length of all parts
  }

  public void write(PrintStream out) {
    try(InputStream in = new BufferedInputStream(descriptor.getReadStream())) {
      parts(in).forEach(p->writePartial(p, out));
    } catch(IOException ex)
    {
    }
  }

  private Stream<HeadedContent> parts(InputStream in) {
    ContentTypeHeader contentTypeHeader = new ContentTypeHeader(descriptor);
    return ranges
      .stream()
      .map(r->new HeadedContent(
            asList(contentTypeHeader, r.getHeader()),
            r.toContent(in)));
  }

  private void writePartial(Content content, PrintStream out) {
    content.write(out);
    out.print(HttpServer.CRLF);
  }
}
