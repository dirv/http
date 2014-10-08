package com.danielirvine.http;

import java.io.*;

public class StringContent implements Content {

  private final String content;

  public StringContent(String content) {
    this.content = content;
  }

  public long length() {
    return content.length();
  }

  public void write(PrintStream out) {
    out.print(content);
  }
}
