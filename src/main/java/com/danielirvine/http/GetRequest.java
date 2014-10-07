package com.danielirvine.http;

import java.io.*;
import java.net.*;
import java.util.*;

public class GetRequest {

  private String path;
  private String query;
  private List<RequestHeader> headers = new ArrayList<RequestHeader>();
  private final DirectoryResource root;

  public GetRequest(InputStream request, DirectoryResource root) throws IOException {
    this.root = root;
    parseRequest(request);
  }

  public Response response() {
    Resource resource = buildResource();
    for(RequestHeader h : headers) {
      resource = h.apply(resource);
    }
    // TODO: possibly push this code into Response
    ResponseCode code = resource.getResponseCode();
    return new Response(code, resource);
  }

  private Resource buildResource() {
    if(query != null) {
      return new QueryResource(buildVariables());
    }
    return root.findResource(path.split("/"));
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

  private Map<String, String> buildVariables() {
    Map<String, String> variables = new HashMap<String, String>();
    String[] keyValues = query.split("&");
    for(String keyValue : keyValues) {
      String[] parts = keyValue.split("=");
      variables.put(parts[0], decode(parts[1]));
    }
    return variables;
  }

  private static String decode(String pathString) {
    try {
      return URLDecoder.decode(pathString, "UTF-8");
    }
    catch(UnsupportedEncodingException ex) {
      return pathString;
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
    if(parts[0].equals("Range")) {
      return new RangeHeader(parts[1].trim());
    }
    else {
      return new UnknownRequestHeader();
    }
  }
}
