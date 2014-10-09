package com.danielirvine.http;

import java.io.*;

import com.danielirvine.http.content.HeadedContent;

public class Response {

  private final ResponseCode code;
  private final HeadedContent body;

  public Response(ResponseCode code, HeadedContent body) {
    this.code = code;
    this.body = body;
  }

  public ResponseCode getResponseCode() {
    return code;
  }

  public void write(OutputStream out) throws IOException {
    try(PrintStream p = new PrintStream(out)) {
      writeStatusLine(p);
      body.write(p);
    }
  }

  private void writeStatusLine(PrintStream out) {
    out.print(HttpServer.PROTOCOL_VERSION);
    out.print(" ");
    out.print(code);
    out.print(HttpServer.CRLF);
  }
}
