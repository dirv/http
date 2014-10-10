package com.danielirvine.http;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import com.danielirvine.http.responses.Response;

public abstract class RequestTest {
  private String requestContent;

  protected void startRequest(String requestLine) {
    requestContent = requestLine + HttpServer.CRLF;
  }

  protected void addHeader(String name, String value) {
    requestContent += name;
    requestContent += ": ";
    requestContent += value;
    requestContent += HttpServer.CRLF;
  }
  
  protected void addData(String content) {
    requestContent += HttpServer.CRLF;
    requestContent += content;
  }

  protected Request buildRequest() {
    try {
      return new Request(new BufferedReader(new StringReader(requestContent)));
    } catch(Exception ex) {
      return null;
    }
  }
  
  protected String readStream(Reader s) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      int b;
      while((b = s.read()) != -1) {
        out.write(b);
      }
    } catch (IOException ex) {
    }
    return out.toString();
  }
  
  protected String readStream(InputStream s) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      int b;
      while((b = s.read()) != -1) {
        out.write(b);
      }
    } catch (IOException ex) {
    }
    return out.toString();
  }
  
  protected static List<String> responseText(Response response) {
    ByteArrayOutputStream s = new ByteArrayOutputStream();
    try{
      response.write(s);
    } catch(IOException ex)
    {
    }
    return Arrays.asList(s.toString().split(HttpServer.CRLF));
  }
}
