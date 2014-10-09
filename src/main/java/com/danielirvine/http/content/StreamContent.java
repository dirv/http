package com.danielirvine.http.content;

import java.io.*;

public class StreamContent implements Content {

  private final long skip;
  private final long length;
  private final InputStream in;

  public StreamContent(long skip, long length, InputStream in) {
    this.skip = skip;
    this.length = length;
    this.in = in;
  }

  public void write(PrintStream out) {
    try {
      in.skip(skip);
      int b;
      long curPos = 0;
      while((b = in.read()) != -1 && ++curPos <= length) {
        out.write(b);
      }
    }
    catch(IOException ex) {
    }
  }

  public long length() {
    return length;
  }
}
