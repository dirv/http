package com.danielirvine.http;

import java.io.*;
import java.util.*;

public class Response {

  private final ResponseCode code;
  private final Resource body;

  public Response(ResponseCode code, Resource body) {
    this.code = code;
    this.body = body;
  }

  public void print(PrintWriter out) {
    addStatusLine(out);
    addHeaders(out);
    endHeader(out);
    addBody(out);
    out.flush();
  }

  private void addBody(PrintWriter out) {
    body.dumpResource(out);
  }

  private void addStatusLine(PrintWriter out) {
    out.print("HTTP/1.1 " + code + HttpServer.CRLF);
  }

  private void addHeaders(PrintWriter out) {
    for(Header h : body.getHeaders()) {
      out.print(h + HttpServer.CRLF);
    }
  }

  private void endHeader(PrintWriter out) {
    out.print(HttpServer.CRLF);
  }
}
