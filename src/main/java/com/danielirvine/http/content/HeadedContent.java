package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.HttpServer;
import com.danielirvine.http.headers.response.ResponseHeader;

public class HeadedContent implements Content {

  private final List<ResponseHeader> headers;
  private final List<Content> content;

  public HeadedContent(List<ResponseHeader> headers, List<Content> content) {
    this.headers = headers;
    this.content = content;
  }

  public void write(PrintStream out) {
    writeHeaders(out);
    endHeader(out);
    writeContents(out);
  }

  public long length() {
    return content.stream().mapToLong(s->s.length()).sum();
  }

  private void writeHeaders(PrintStream out) {
    for(ResponseHeader h : headers) {
      h.write(out);
    }
  }

  private void writeContents(PrintStream out) {
    for(Content c : content) {
      c.write(out);
    }
  }

  private void endHeader(PrintStream out) {
    out.print(HttpServer.CRLF);
  }
}
