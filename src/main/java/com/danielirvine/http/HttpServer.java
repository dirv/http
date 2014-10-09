package com.danielirvine.http;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import static java.util.Arrays.*;

public class HttpServer {

  public static final String CRLF = "\r\n";
  public static final String PROTOCOL_VERSION = "HTTP/1.1";
  private final Responder responder;
  private final Logger logger;

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory,
      int port,
      String publicRoot,
      InputStream redirectStream,
      InputStream authStream,
      List<String> writeablePaths) {
    this(socketFactory.apply(port),
        new FsFileDescriptor(new File(publicRoot)),
        redirectStream,
        authStream,
        writeablePaths);
  }

  public HttpServer(ServerSocketProxy socket,
      FileDescriptor rootFile,
      InputStream redirectsStream,
      InputStream authStream,
      List<String> writeablePaths) {
    DirectoryResource root = new DirectoryResource(rootFile);
    UrlRedirects redirects = new UrlRedirects(redirectsStream);
    Authorizor authorizor = new Authorizor(authStream);
    logger = new Logger();

    responder = new Responder(asList(
          new UnauthorizedResponseContributor(authorizor),
          new RedirectResponseContributor(redirects),
          new QueryResponseContributor(),
          new LogsResponseContributor(logger),
          new PutPostResponseContributor(root),
          new ResourceResponseContributor(root),
          new WriteableResponseContributor(writeablePaths),
          new NotFoundResponseContributor()));

    while(socket.hasData()) {
      try(SocketProxy clientSocket = socket.accept()) {
        handleIncomingRequest(clientSocket);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private static List<String> resourceToStrings(String resourceName) throws IOException {
    List<String> allLines = new ArrayList<String>();
    try(InputStream in = HttpServer.class.getResourceAsStream(resourceName)){
      try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        String currentLine = null;
        while((currentLine = reader.readLine()) != null) {
          allLines.add(currentLine);
        }
      }
    }
    return allLines;
  }

  public static void main(String[] args) throws IOException {
    ArgumentParser parser = new ArgumentParser(args);

    InputStream redirectStream = HttpServer.class.getResourceAsStream("/redirects.txt");
    InputStream authStream = HttpServer.class.getResourceAsStream("/access.txt");
    new HttpServer(HttpServer::createSocket,
        parser.get("p", 5000),
        parser.get("d", ""),
        redirectStream,
        authStream,
        resourceToStrings("/writeable.txt"));
  }

  private void handleIncomingRequest(SocketProxy socket) throws IOException {
    Request request = new Request(socket.getInputStream());
    logger.log(request);
    Response response = responder.response(request);
    response.write(socket.getOutputStream());
  }

  private static ServerSocketProxy createSocket(int port) {
    try {
      return new NetServerSocket(port);
    }
    catch(IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
