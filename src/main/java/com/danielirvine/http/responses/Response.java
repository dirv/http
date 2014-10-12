package com.danielirvine.http.responses;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.danielirvine.http.HttpServer;
import com.danielirvine.http.headers.response.ResponseHeader;

public class Response {

  private final ResponseCode code;
  private final List<ResponseHeader> headers;

  public Response(ResponseCode code, List<ResponseHeader> headers) {
    this.code = code;
    this.headers = headers;
  }

  public ResponseCode getResponseCode() {
    return code;
  }

  public void write(OutputStream out) throws IOException {
    writeStatusLine(out);
    writeHeaders(out);
  }

  private void writeStatusLine(OutputStream out) throws IOException {
    writeString(out, HttpServer.PROTOCOL_VERSION + " " + code + HttpServer.CRLF);
  }
  
  private void writeHeaders(OutputStream out) throws IOException {
    for(ResponseHeader h : headers) {
      h.write(out);
    }
    writeString(out, HttpServer.CRLF);
  }

  private void writeString(OutputStream out, String string) throws IOException {
    out.write(string.getBytes());
  }
}