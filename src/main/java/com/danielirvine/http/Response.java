package com.danielirvine.http;

import java.io.*;

public class Response {

  private final ResponseCode code;
  private GetRequest request;

  public Response(ResponseCode code) {
    this.code = code;
  }

  public void print(PrintWriter out) {
    addStatusLine(out);
    endHeader(out);
    addBody(out);
  }
  public void setBody(GetRequest request) {
    this.request = request;
  }

  private void addBody(PrintWriter out) {
    if(request != null) {
      request.dumpResource(out);
    }
    out.flush();
  }

  private void addStatusLine(PrintWriter out) {
    out.print("HTTP/1.1 " + code + HttpServer.CRLF);
  }

  private void endHeader(PrintWriter out) {
    out.print(HttpServer.CRLF);
  }
}
