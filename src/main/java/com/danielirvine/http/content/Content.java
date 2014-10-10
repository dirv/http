package com.danielirvine.http.content;

import java.io.*;

public interface Content {
  public void write(PrintStream out);
  public long length();
  public String type();
  public Content withRange(Range range);
}
