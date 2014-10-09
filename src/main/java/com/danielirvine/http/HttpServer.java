package com.danielirvine.http;

import java.io.*;
import java.net.*;
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
      InputStream authStream) {
    this(socketFactory.apply(port),
        new FsFileDescriptor(new File(publicRoot)),
        redirectStream,
        authStream);
  }

  public HttpServer(ServerSocketProxy socket,
      FileDescriptor rootFile,
      InputStream redirectsStream,
      InputStream authStream) {
    DirectoryResource root = new DirectoryResource(rootFile);
    UrlRedirects redirects = new UrlRedirects(redirectsStream);
    Authorizor authorizor = new Authorizor(authStream);
    logger = new Logger();

    responder = new Responder(asList(
          new UnauthorizedResponseContributor(authorizor),
          new RedirectResponseContributor(redirects),
          new QueryResponseContributor(),
          new LogsContributor(logger),
          new ResourceResponseContributor(root),
          new NotFoundResponseContributor()));

    while(socket.hasData()) {
      try(SocketProxy clientSocket = socket.accept()) {
        handleIncomingRequest(clientSocket);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser = new ArgumentParser(args);

    InputStream redirectStream = HttpServer.class.getResourceAsStream("/redirects.txt");
    InputStream authStream = HttpServer.class.getResourceAsStream("/access.txt");
    new HttpServer(HttpServer::createSocket,
        parser.get("p", 5000),
        parser.get("d", ""),
        redirectStream,
        authStream);
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
