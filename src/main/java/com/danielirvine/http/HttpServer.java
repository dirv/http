package com.danielirvine.http;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.sockets.*;

public class HttpServer {

  public static final String CRLF = "\r\n";
  public static final String PROTOCOL_VERSION = "HTTP/1.1";

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
    Logger logger = new Logger();
    Responder responder = new Responder(logger, writeablePaths,
        new DirectoryResource(rootFile),
        new UrlRedirects(redirectStrings),
        new Authorizer(authTable),
        new InMemoryResourceCache());
    
    try {
      while(socket.hasData()) {
        executor.execute(new RequestReceiver(logger, responder, socket.accept()));
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