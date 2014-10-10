package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

public interface Content {
  public void write(PrintStream out);
  public long length();
  public String type();
  public List<Content> withRanges(List<FixedRange> range);
  public List<Header> additionalHeaders();
}
