package com.danielirvine.http;

import java.io.*;
import java.util.*;

public class Request {

  private String path;
  private String query;
  private String user;
  private String verb;
  private String password;
  private String requestLine;
  private List<RequestHeader> headers = new ArrayList<RequestHeader>();

  public Request(InputStream request) throws IOException {
    parseRequest(request);
  }

  public boolean isPut() {
    return verb.equals("PUT");
  }

  public boolean isPost() {
    return verb.equals("POST");
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

  public String[] getPathSegments() {
    return path.split("/");
  }

  public boolean hasCredentials() {
    return user != null && password != null;
  }

  public void setCredentials(String user, String password) {
    this.user = user;
    this.password = password;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public String getRequestLine() {
    return requestLine;
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
    requestLine = in.readLine();
    String[] parts = requestLine.split(" ");
    verb = parts[0];
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
    while(!isNullOrBlank((headerString = in.readLine()))) {
      headers.add(RequestHeader.build(this, headerString));
    }
  }

  public static boolean isNullOrBlank(String param) {
    return param == null || param.trim().length() == 0;
  }
}
