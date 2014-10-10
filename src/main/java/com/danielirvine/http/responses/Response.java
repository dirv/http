package com.danielirvine.http.responses;

import java.io.*;

import com.danielirvine.http.HttpServer;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.headers.response.ContentLengthHeader;
import com.danielirvine.http.headers.response.ResponseHeader;

public class Response {

  private final ResponseCode code;
  private final Content body;

  public Response(ResponseCode code, Content body) {
    this.code = code;
    this.body = body;
  }

  public ResponseCode getResponseCode() {
    return code;
  }

  public void write(OutputStream out) throws IOException {
    try(PrintStream p = new PrintStream(out)) {
      writeStatusLine(p);
      writeHeaders(p);
      body.write(p);
    }
  }

  private void writeStatusLine(PrintStream out) {
    out.print(HttpServer.PROTOCOL_VERSION);
    out.print(" ");
    out.print(code);
    out.print(HttpServer.CRLF);
  }

  private void writeHeaders(PrintStream out) {

    out.print(body.contentType());
    out.print(new ContentLengthHeader(body.length()));
    for(ResponseHeader h : body.additionalHeaders()) {
      out.print(h);
    }
    endHeader(out);
  }

  private void endHeader(PrintStream out) {
    out.print(HttpServer.CRLF);
  }
}
