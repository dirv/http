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
    server = new HttpServer(socket);
    assertEquals("HTTP/1.1 200 OK\n", socket.getOutput());
  }

  @Test
	public void fourOhFour() {

    String text = "GET /foobar HTTP/1.1\n";
    InProcessServerSocket socket = new InProcessServerSocket(text);
    server = new HttpServer(socket);
    assertEquals("HTTP/1.1 404 Not Found\n", socket.getOutput());
  }

}
