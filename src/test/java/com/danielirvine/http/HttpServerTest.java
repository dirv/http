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
    Function<Integer, ServerSocketProxy> socketFactory = (port) -> {
      portSpecified = port;
      return null;
    };
    server = new HttpServer(socketFactory, 212);
    assertEquals(212, portSpecified);
  }

  @Test
	public void getRequestForRootReturnsOK() {
    String text = "GET / HTTP/1.1\n";
    InProcessServerSocket socket = new InProcessServerSocket(text);
    Function<Integer, ServerSocketProxy> socketFactory = (port) -> {
      portSpecified = port;
      return socket;
    };

    server = new HttpServer(socketFactory, 212);
    assertEquals("HTTP/1.1 200 OK\n", socket.getOutput());
  }

}
