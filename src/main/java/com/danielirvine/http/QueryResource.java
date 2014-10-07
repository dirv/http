package com.danielirvine.http;
import java.util.*;
import java.io.*;

class QueryResource implements Resource {

  private final Map<String, String> variables;
  private String content;

  public QueryResource(Map<String, String> variables) {
    this.variables = variables;
  }

  public Resource applyRange(RangeHeader range) {
    // TODO: Could actually apply a range at this point since
    // the content has been generated (or at least could be)
    return this;
  }

  public void dumpResource(OutputStream out) {
    ensureContentIsGenerated();
    try {
      out.write(content.getBytes());
    } catch(IOException ex) {
    }
  }

  private void generateContent() {
    content = "";
    for(Map.Entry<String, String> variable : variables.entrySet()) {
      content += variable.getKey() + " = " + variable.getValue();
      content += HttpServer.CRLF;
    }
  }

  public List<ResponseHeader> getHeaders() {
    ensureContentIsGenerated();
    List<ResponseHeader> headers = new ArrayList<ResponseHeader>();
    headers.add(ContentTypeHeader.TEXT_PLAIN);
    headers.add(new ContentLengthHeader(content.length()));
    return headers;
  }

  public ResponseCode getResponseCode() {
    return ResponseCode.OK;
  }

  private void ensureContentIsGenerated() {
    if (content == null) {
      generateContent();
    }
  }
}

