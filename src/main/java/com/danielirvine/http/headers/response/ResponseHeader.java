package com.danielirvine.http.headers.response;

import java.io.*;

import com.danielirvine.http.HttpServer;

public class ResponseHeader {

  private final String header;
  private final String value;
  private final String[] parameters;

  public ResponseHeader(String header, String value, String... parameters) {
    this.header = header;
    this.value = value;
    this.parameters = parameters;
  }

  public void write(OutputStream out) throws IOException {
    out.write((toString() + HttpServer.CRLF).getBytes());
  }

  @Override
  public String toString() {
    String str = header + ": " + value;
    for(String parameter : parameters) {
      str += "; " + parameter;
    }
    return str;
  }
}
