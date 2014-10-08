package com.danielirvine.http;

import java.io.*;

public interface Content {
  public void write(PrintStream out);
  public long length();
}
