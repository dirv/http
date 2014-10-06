package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.util.function.*;

public class HttpServerTest {

  private HttpServer server;
  private InProcessServerSocket socket;
  private final InMemoryFileDescriptor publicRoot = new InMemoryFileDescriptor("publicRoot");
  private int portSpecified = 0;

  @Test
  public void opensSocketOnPort() throws Exception {
    Function<Integer, ServerSocketProxy> socketFactory = (port) -> {
      portSpecified = port;
      return null;
    };
    server = new HttpServer(socketFactory, 212, ".");
    assertEquals(212, portSpecified);
  }

  @Test
	public void getRequestForRootReturnsOK() {
    createGetRequest("/");
    createServer();
    assertEquals("HTTP/1.1 200 OK", outputByLine()[0]);
  }

  @Test
	public void fourOhFour() {
    createGetRequest("/foobar");
    createServer();
    assertEquals("HTTP/1.1 404 Not Found", outputByLine()[0]);
  }

  @Test
	public void fileContents() {
    publicRoot.addFile("testFile", "content");
    createGetRequest("/testFile");
    createServer();
    assertEquals("HTTP/1.1 200 OK", outputByLine()[0]);
    assertEquals("", outputByLine()[1]);
    assertEquals("content", outputByLine()[2]);
  }

  private void createGetRequest(String path) {
    String requestLine = "GET " + path + " HTTP/1.1\n";
    socket = new InProcessServerSocket(requestLine);
  }

  private void createServer() {
    server = new HttpServer(socket, publicRoot);
  }

  private String[] outputByLine() {
    return socket.getOutput().split(HttpServer.CRLF);
  }
}
