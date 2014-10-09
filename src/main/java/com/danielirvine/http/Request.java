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
  private final Reader in;
  private List<RequestHeader> headers = new ArrayList<RequestHeader>();
  private long contentLength;

  public Request(BufferedReader in) throws IOException {
    this.in = in;
    readRequestLine(in);
    readHeaders(in);
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

  public Reader getDataStream() {
    return new Reader() {
      
      private long curPos;

      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        long dataLeft = contentLength - curPos;
        
        if ((long)len > dataLeft) {
          in.read(cbuf, off, (int)dataLeft);
          return -1;
        } else {
          curPos += len;
          return in.read(cbuf, off, len);
        }
      }

      @Override
      public void close() throws IOException {
        in.close();
      }
    };
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

  public void setContentLength(long length) {
    this.contentLength = length;
  }

  public boolean isDelete() {
    return verb.equals("DELETE");
  }
}
