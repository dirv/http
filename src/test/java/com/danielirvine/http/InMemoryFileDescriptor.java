package com.danielirvine.http;

import java.io.*;

import java.util.*;

public class InMemoryFileDescriptor implements FileDescriptor {
  private final String name;
  private String contents;
  private final List<FileDescriptor> children;

  public InMemoryFileDescriptor(String name) {
    this(name, null);
  }

  public InMemoryFileDescriptor(String name, String contents) {
    this.name = name;
    this.children = new ArrayList<FileDescriptor>();
    this.contents = contents;
  }

  public String getName() {
    return name;
  }

  public FileDescriptor getFile(String name) {
    for(FileDescriptor child : children) {
      if (child.getName().equals(name)) {
        return child;
      }
    }
    return new InMemoryFileDescriptor(null);
  }

  public FileDescriptor createFile(String name) {
    InMemoryFileDescriptor descriptor = new InMemoryFileDescriptor(name);
    children.add(descriptor);
    return descriptor;
  }

  public long length() {
    return contents.length();
  }

  public void addFile(String name, String contents) {
    children.add(new InMemoryFileDescriptor(name, contents));
  }

  public InputStream getReadStream() {
    return new ByteArrayInputStream(contents.getBytes());
  }

  public List<FileDescriptor> getChildren() {
    return children;
  }

  public String contentType() {
    int extIndex = name.lastIndexOf(".");
    if(extIndex != -1) {
      String ext = name.substring(extIndex+1);
      if (ext.equals("jpeg")) {
        return "image/jpeg";
      }
    }
    return "text/plain";
  }

  public boolean exists() {
    return name != null;
  }

  public void write(Reader in) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int currentByte;
    try {
      while ((currentByte = in.read()) != -1) {
        out.write(currentByte);
      }
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
    contents = out.toString(); 
  }
}
