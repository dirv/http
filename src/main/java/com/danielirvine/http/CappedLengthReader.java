package com.danielirvine.http;

import java.io.IOException;
import java.io.Reader;

class CappedLengthReader extends Reader {
  private final Reader in;
  private final long maxLength;

  CappedLengthReader(Reader in, long maxLength) {
    this.in = in;
    this.maxLength = maxLength;
  }

  private long curPos;

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    long dataLeft = maxLength - curPos;
    
    if ((long)len > dataLeft) {
      in.read(cbuf, off, (int)dataLeft);
      return -1;
    } else {
      curPos += len;
      return in.read(cbuf, off, len);
    }
  }

  public void close() throws IOException {
    in.close();
  }
}