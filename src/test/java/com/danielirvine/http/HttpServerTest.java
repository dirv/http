package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.net.*;
import java.util.function.*;
import java.util.*;

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
    assertEquals("HTTP/1.1 200 OK", outputByLine().get(0));
  }

  @Test
	public void fourOhFour() {
    createGetRequest("/foobar");
    createServer();
    assertEquals("HTTP/1.1 404 Not Found", outputByLine().get(0));
  }

  @Test
	public void fileContents() {
    publicRoot.addFile("testFile", "content");
    createGetRequest("/testFile");
    createServer();
    assertEquals("HTTP/1.1 200 OK", outputByLine().get(0));
    assertThat(outputByLine(), hasItem(containsString("content")));
  }

  @Test
  public void directoryListing() {
    publicRoot.addFile("test1", "content");
    publicRoot.addFile("test2", "content");
    createGetRequest("/");
    createServer();
    assertThat(outputByLine(), hasItem(containsString("test1")));
    assertThat(outputByLine(), hasItem(containsString("test2")));
  }

  @Test
	public void dumpLinksWhenListingDirectory() {
    publicRoot.addFile("test1", "content");
    createGetRequest("/");
    createServer();
    assertThat(outputByLine(), hasItem("<a href=\"/test1\">test1</a>"));
  }

  private void createGetRequest(String path) {
    String requestLine = "GET " + path + " HTTP/1.1\n";
    socket = new InProcessServerSocket(requestLine);
  }

  private void createServer() {
    server = new HttpServer(socket, publicRoot);
  }

  private List<String> outputByLine() {
    return Arrays.asList(socket.getOutput().split(HttpServer.CRLF));
  }
}
