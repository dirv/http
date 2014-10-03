package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.util.function.*;

public class HttpServerTest {

  private HttpServer server;
  private int portSpecified = 0;

  @Test
  public void opensSocketOnPort() throws Exception {
    Function<Integer, ServerSocket> socketFactory = (port) -> {
      portSpecified = port;
      return null;
    };
    server = new HttpServer(socketFactory, 212);
    assertEquals(212, portSpecified);
  }

}
