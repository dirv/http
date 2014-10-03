package com.danielirvine.http;

import java.net.*;
import java.util.function.*;

public class HttpServer {

  public HttpServer(Function<Integer, ServerSocket> socketFactory, int port) {
    try {
      ServerSocket socket = socketFactory.apply(port);
    }
    catch(Exception ex) {
      System.out.println(ex);
    }
  }
}
