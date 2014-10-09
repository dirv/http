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

  public void write(PrintStream out) {
    out.print(toString());
    out.print(HttpServer.CRLF);
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
