package com.danielirvine.http;

import java.io.*;
import java.util.*;

import com.danielirvine.http.headers.request.RequestHeader;
import com.danielirvine.http.ranges.Range;

public class Request {

  private String path;
  private String query;
  private String user;
  private RequestMethod method;
  private String password;
  private String requestLine;
  private final Reader in;
  private List<RequestHeader> headers = new ArrayList<RequestHeader>();
  private long contentLength;
  private List<Range> ranges;
  private String eTag;

  public Request(BufferedReader in) throws IOException {
    this.in = in;
    readRequestLine(in);
    readHeaders(in);
  }

  public RequestMethod getMethod() {
    return method; 
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

  public boolean hasRanges() {
    return ranges != null && ranges.size() > 0;
  }

  public List<Range> getRanges() {
    return ranges;
  }

  public void setRanges(List<Range> ranges) {
    this.ranges = ranges;
  }

  public void setContentLength(long length) {
    this.contentLength = length;
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

  public Reader getDataStream() {
    return new CappedLengthReader(in, contentLength);
  }

  private void readRequestLine(BufferedReader in) throws IOException {
    requestLine = in.readLine();
    String[] parts = requestLine.split(" ");
    method = RequestMethod.valueOf(parts[0]);
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

  private static boolean isNullOrBlank(String param) {
    return param == null || param.trim().length() == 0;
  }

  public void setETag(String eTag) {
    this.eTag = eTag;
  }

  public String getETag() {
    return eTag;
  }
}
