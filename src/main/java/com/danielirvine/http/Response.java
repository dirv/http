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

  public void print(Writer out) throws IOException {
    addStatusLine(out);
    addHeaders(out);
    endHeader(out);
    addBody(out);
    out.flush();
  }

  private void addBody(Writer out) {
    body.dumpResource(out);
  }

  private void addStatusLine(Writer out) throws IOException {
    out.write("HTTP/1.1 " + code + HttpServer.CRLF);
  }

  private void addHeaders(Writer out) throws IOException {
    for(ResponseHeader h : body.getHeaders()) {
      out.write(h + HttpServer.CRLF);
    }
  }

  private void endHeader(Writer out) throws IOException {
    out.write(HttpServer.CRLF);
  }
}
