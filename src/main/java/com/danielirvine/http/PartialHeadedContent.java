package com.danielirvine.http;

import java.io.*;
import java.util.*;
import static java.util.Arrays.*;

class PartialHeadedContent implements Content {
  private final FileDescriptor descriptor;
  private final List<FixedRangeSpecifier> ranges;

  public PartialHeadedContent(FileDescriptor descriptor, List<FixedRangeSpecifier> ranges) {
    this.descriptor = descriptor;
    this.ranges = ranges;
  }

  public long length() {
    return descriptor.length(); // TODO - should possibly be total length of all parts
  }

  public void write(PrintStream out) {
    long totalLength = descriptor.length();
    try(InputStream in = new BufferedInputStream(descriptor.getReadStream())) {
      for(HeadedContent content : parts(in)) {
        content.write(out);
        out.print(HttpServer.CRLF);
        // TODO: output boundary
      }
    } catch(IOException ex)
    {
    }
  }

  private List<HeadedContent> parts(InputStream in) {

    // TODO: get rid of FixedRangeSpecifier here, I don't think it's helping.
    // Just fix it at the the time we calculate parts.
    long curPos = 0;
    List<HeadedContent> contents = new ArrayList<HeadedContent>();
    for(FixedRangeSpecifier range : ranges) {
      StreamContent content = createContent(curPos, range, in);
      curPos += content.length() + 1;
      contents.add(new HeadedContent(asList(new ContentTypeHeader(descriptor),
            range.toHeader()), asList(content)));
    }
    return contents;
  }

  private StreamContent createContent(long curPos, FixedRangeSpecifier range, InputStream in) {
    long skip = range.getLow() - curPos;
    long length = range.length();
    return new StreamContent(skip, length, in);
  }
}
