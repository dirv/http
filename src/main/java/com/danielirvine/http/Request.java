package com.danielirvine.http;

import java.io.*;
import java.util.*;

public class Request {

  private String path;
  private String query;
  private List<RequestHeader> headers = new ArrayList<RequestHeader>();

  public Request(InputStream request) throws IOException {
    parseRequest(request);
  }

  public boolean hasQuery() {
    return query != null;
  }

  public String getQuery() {
    return query;
  }

  public String getPath() {
    return path;
  }

  public List<RequestHeader> getHeaders() {
    return headers;
  }

  private void parseRequest(InputStream request) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(request));
    readRequestLine(in);
    readHeaders(in);
  }

  private void readRequestLine(BufferedReader in) throws IOException {
    String requestLine = in.readLine();
    String[] parts = requestLine.split(" ");
    int queryIndex = parts[1].indexOf("?");
    if(queryIndex == -1) {
      path = parts[1];
    } else {
      path = parts[1].substring(0, queryIndex);
      query = parts[1].substring(queryIndex+1);
    }
  }

  private void readHeaders(BufferedReader in) throws IOException {
    String headerString = null;
    while((headerString = in.readLine()) != null) {
      headers.add(buildHeader(headerString));
      // TODO: fix this break
      if (headerString.equals("")) break;
    }
  }

  private RequestHeader buildHeader(String headerString) {
    String[] parts = headerString.split(":");
    // TODO: parse this using a map
    if(parts[0].equals("Range")) {
      return new RangeHeader(parts[1].trim());
    }
    else {
      return new UnknownRequestHeader();
    }
  }
}
