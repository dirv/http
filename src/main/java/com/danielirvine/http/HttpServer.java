package com.danielirvine.http;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.sockets.*;

public class HttpServer {

  public static final String CRLF = "\r\n";
  public static final String PROTOCOL_VERSION = "HTTP/1.1";
  private final Responder responder;
  private final Logger logger;

  public HttpServer(Function<Integer, ServerSocketProxy> socketFactory,
      Executor executor,
      int port,
      String publicRoot,
      List<String> redirectStrings,
      List<String> authTable,
      List<String> writeablePaths) {
    this(socketFactory.apply(port),
        executor,
        new FsFileDescriptor(new File(publicRoot)),
        redirectStrings,
        authTable,
        writeablePaths);
  }

  public HttpServer(ServerSocketProxy socket,
      Executor executor,
      FileDescriptor rootFile,
      List<String> redirectStrings,
      List<String> authTable,
      List<String> writeablePaths) {
    DirectoryResource root = new DirectoryResource(rootFile);
    UrlRedirects redirects = new UrlRedirects(redirectStrings);
    Authorizer authorizer = new Authorizer(authTable);
    InMemoryResourceCache cache = new InMemoryResourceCache();
    this.logger = new Logger();

    responder = new Responder(logger, writeablePaths, root, redirects, authorizer, cache);
    
    try {
      while(socket.hasData()) {
        executor.execute(new SocketHandler(socket.accept()));
      }
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    ArgumentParser parser = new ArgumentParser(args);
    ExecutorService executor = Executors.newCachedThreadPool();
    new HttpServer(HttpServer::createSocket,
        executor,
        parser.get("p", 5000),
        parser.get("d", "."),
        resourceToStrings("/redirects.txt"),
        resourceToStrings("/access.txt"),
        resourceToStrings("/writeable.txt"));
    executor.shutdown();
  }

  class SocketHandler implements Runnable {
    private final SocketProxy socket;

    SocketHandler(SocketProxy socket) { 
      this.socket = socket;
    }

    public void run() {
      try {
        handleIncomingRequest(socket);
      } catch(Exception ex) {
      }
    }

    private void handleIncomingRequest(SocketProxy socket) throws IOException {
      try(InputStream in = socket.getInputStream()) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
          Request request = new Request(reader);
          logger.log(request);
          Response response = responder.response(request);
          try(BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
            response.write(output);
          }
        }
      }
      socket.close();
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
