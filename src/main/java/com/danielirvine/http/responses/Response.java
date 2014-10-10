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
    writeStatusLine(out);
    writeHeaders(out);
    body.write(out);
    out.flush();
  }

  private void writeStatusLine(OutputStream out) throws IOException {
    writeString(out, HttpServer.PROTOCOL_VERSION + " " + code + HttpServer.CRLF);
  }

  private void writeHeaders(OutputStream out) throws IOException {
    body.contentType().write(out);
    new ContentLengthHeader(body.length()).write(out);
    for(ResponseHeader h : body.additionalHeaders()) {
      h.write(out);
    }
    writeString(out, HttpServer.CRLF);
  }
  
  private void writeString(OutputStream out, String string) throws IOException {
    out.write(string.getBytes());
  }
}
