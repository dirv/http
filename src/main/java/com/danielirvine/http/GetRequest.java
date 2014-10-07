package com.danielirvine.http;

import java.io.*;
import java.util.*;

public class GetRequest {

  private String target;
  private List<RangeHeader> headers = new ArrayList<RangeHeader>();
  private final DirectoryResource root;

  public GetRequest(InputStream request, DirectoryResource root) throws IOException {
    this.root = root;
    parseRequest(request);
  }

  public Response response() {
    Resource resource = buildResource();
    for(RangeHeader h : headers) {
      resource = ((FileResource)resource).applyRange(h);
    }
    ResponseCode code = resource.getResponseCode();
    return new Response(code, resource);
  }

  private Resource buildResource() {
    return root.findResource(target.split("/"));
  }

  private void parseRequest(InputStream request) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(request));
    readRequestLine(in);
    readHeaders(in);
  }

  private void readRequestLine(BufferedReader in) throws IOException {
    String requestLine = in.readLine();
    String[] parts = requestLine.split(" ");
    this.target = parts[1];
  }

  private void readHeaders(BufferedReader in) throws IOException {
    String headerString = null;
    while((headerString = in.readLine()) != null) {
      headers.add(buildHeader(headerString));
    }
  }

  private RangeHeader buildHeader(String headerString) {
    String[] parts = headerString.split(": ");
    return new RangeHeader(parts[1]);
  }
}
