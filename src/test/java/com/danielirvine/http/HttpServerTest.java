package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.function.*;
import java.util.*;

public class HttpServerTest {

  private InProcessServerSocket socket;
  private final InMemoryFileDescriptor publicRoot = new InMemoryFileDescriptor("publicRoot");
  private int portSpecified = 0;
  private List<String> redirects = new ArrayList<String>();
  private List<String> authTable = new ArrayList<String>();
  private List<String> writeablePaths = new ArrayList<String>();

  @Test
  public void opensSocketOnPort() throws Exception {
    Function<Integer, ServerSocketProxy> socketFactory = (port) -> {
      portSpecified = port;
      return new InProcessServerSocket(new String[0]);
    };
    new HttpServer(socketFactory, 212, ".", redirects, authTable, writeablePaths);
    assertEquals(212, portSpecified);
  }

  @Test
	public void decodesUrlParameters() {
    createGetRequest("/parameters?hi=%20%3C%2C");
    createServer();
    assertThat(socket.getOutput(0), containsString("hi =  <,"));
  }

  @Test
	public void servesMultipleRequests() {
    createGetRequest("/", "/");
    createServer();
    assertThat(socket.getOutput(0), containsString("HTTP/1.1 200 OK"));
    assertThat(socket.getOutput(1), containsString("HTTP/1.1 200 OK"));
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
    assertThat(outputByLine(), hasItem(containsString("<a href=\"/test1\">test1</a>")));
  }

  @Test
	public void redirects() {
    redirects.add("/a => /b");
    publicRoot.addFile("b", "hello");
    createGetRequest("/a");
    createServer();
    assertEquals("HTTP/1.1 301 Moved permanently", outputByLine().get(0));
    assertThat(outputByLine(), hasItem(containsString("/b")));
  }

  @Test
	public void promptsForAuthorization() {
    authTable.add("/a:admin:xs");
    createGetRequest("/a");
    createServer();
    assertEquals("HTTP/1.1 401 Authentication required", outputByLine().get(0));
    assertThat(outputByLine(), hasItem(containsString("WWW-Authenticate: Basic realm=\"/a\"")));
  }

  @Test
	public void showsLogs() {
    createGetRequest("/a", "/b", "/logs");
    createServer();
    assertThat(requestOutput(2), hasItem(containsString("GET /a HTTP/1.1")));
    assertThat(requestOutput(2), hasItem(containsString("GET /b HTTP/1.1")));
  }
  private void createGetRequest(String... paths) {
    String[] requests = new String[paths.length];
    for(int i = 0; i < paths.length; ++i){
      requests[i] = createGetRequestFromPath(paths[i]);
    }
    socket = new InProcessServerSocket(requests);
  }

  private static String createGetRequestFromPath(String path) {
    return "GET " + path + " HTTP/1.1" + HttpServer.CRLF;
  }

  private void createServer() {
    new HttpServer(socket, publicRoot, redirects, authTable, writeablePaths);
  }

  private List<String> outputByLine() {
    return requestOutput(0);
  }

  private List<String> requestOutput(int number){
    return Arrays.asList(socket.getOutput(number).split(HttpServer.CRLF));
  }
}
