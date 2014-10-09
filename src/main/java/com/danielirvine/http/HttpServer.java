package com.danielirvine.http;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class HttpServer {

  public static final String CRLF = "\r\n";
  public static final String PROTOCOL_VERSION = "HTTP/1.1";
  private final Responder responder;
  private final Logger logger;

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory,
      int port,
      String publicRoot,
      List<String> redirectStrings,
      List<String> authTable,
      List<String> writeablePaths) {
    this(socketFactory.apply(port),
        new FsFileDescriptor(new File(publicRoot)),
        redirectStrings,
        authTable,
        writeablePaths);
  }

  public HttpServer(ServerSocketProxy socket,
      FileDescriptor rootFile,
      List<String> redirectStrings,
      List<String> authTable,
      List<String> writeablePaths) {
    DirectoryResource root = new DirectoryResource(rootFile);
    UrlRedirects redirects = new UrlRedirects(redirectStrings);
    Authorizer authorizer = new Authorizer(authTable);
    logger = new Logger();

    responder = new Responder(logger, writeablePaths, root, redirects, authorizer);

    while(socket.hasData()) {
      try(SocketProxy clientSocket = socket.accept()) {
        handleIncomingRequest(clientSocket);
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws IOException {
    ArgumentParser parser = new ArgumentParser(args);
    new HttpServer(HttpServer::createSocket,
        parser.get("p", 5000),
        parser.get("d", ""),
        resourceToStrings("/redirects.txt"),
        resourceToStrings("/access.txt"),
        resourceToStrings("/writeable.txt"));
  }

  private void handleIncomingRequest(SocketProxy socket) throws IOException {
    try(InputStream in = socket.getInputStream()) {
      try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        Request request = new Request(reader);
        logger.log(request);
        Response response = responder.response(request);
        try(OutputStream output = socket.getOutputStream()) {
          response.write(output);
        }
      }
    }
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

}
