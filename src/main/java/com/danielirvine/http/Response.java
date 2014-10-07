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

  public void print(OutputStream out) throws IOException {
    addStatusLine(out);
    addHeaders(out);
    endHeader(out);
    addBody(out);
    out.flush();
  }

  private void addBody(OutputStream out) {
    body.dumpResource(out);
  }

  private void addStatusLine(OutputStream out) throws IOException {
    out.write(("HTTP/1.1 " + code + HttpServer.CRLF).getBytes());
  }

  private void addHeaders(OutputStream out) throws IOException {
    for(ResponseHeader h : body.getHeaders()) {
      out.write((h + HttpServer.CRLF).getBytes());
    }
  }

  private void endHeader(OutputStream out) throws IOException {
    out.write(HttpServer.CRLF.getBytes());
  }
}
