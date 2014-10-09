package com.danielirvine.http;

import java.io.*;

class SinglePartContent implements Content {

  private final FileDescriptor descriptor;
  private final FixedRange range;

  public SinglePartContent(FileDescriptor descriptor, FixedRange range) {
    this.descriptor = descriptor;
    this.range = range;
  }

  public long length() {
    return descriptor.length();
  }

  public void write(PrintStream out) {
    try(InputStream in = new BufferedInputStream(descriptor.getReadStream())) {
      range.toContent(in).write(out);
    } catch(IOException ex)
    {
    }
  }
}
