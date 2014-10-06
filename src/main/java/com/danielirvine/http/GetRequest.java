package com.danielirvine.http;
import java.io.*;

public class GetRequest {

  private final String target;
  private final FileDescriptor publicRoot;

  public GetRequest(String requestLine, FileDescriptor publicRoot) {
    String[] parts = requestLine.split(" ");
    this.target = parts[1];
    this.publicRoot = publicRoot;
  }

  public boolean targetExists() {
    String fileName = stripRootDirectory(target);
    if (fileName.equals("")) {
      return true;
    } else {
      return publicRoot.getFile(fileName) != null;
    }
  }

  public void dumpResource(PrintWriter out) {
    String fileName = stripRootDirectory(target);
    if (!fileName.equals("")) {
      InputStream reader = publicRoot.getFile(fileName).getReadStream();
      try {
        int b;
        while((b = reader.read()) != -1) {
          out.write(b);
        }
        out.flush();
      } catch(IOException ex) {
        System.err.println(ex);
      }
    }
  }

  private String stripRootDirectory(String target) {
    return target.substring(1);
  }
}
