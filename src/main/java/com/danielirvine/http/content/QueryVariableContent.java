package com.danielirvine.http.content;

import java.io.*;
import java.net.*;

import com.danielirvine.http.HttpServer;

public class QueryVariableContent extends StringContent {
  public QueryVariableContent(String name, String encodedValue) {
    super(name + " = " + decode(encodedValue) + HttpServer.CRLF);
  }

  private static String decode(String pathString) {
    try {
      return URLDecoder.decode(pathString, "UTF-8");
    }
    catch(UnsupportedEncodingException ex) {
      return pathString;
    }
  }
}
