package com.danielirvine.http;

import java.io.*;

public class Response {

  private final ResponseCode code;
  private final Resource body;

  public Response(ResponseCode code, Resource body) {
    this.code = code;
    this.body = body;
  }

  public void print(PrintWriter out) {
    addStatusLine(out);
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

  private void endHeader(PrintWriter out) {
    out.print(HttpServer.CRLF);
  }
}
