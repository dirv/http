package com.danielirvine.http;

import java.io.*;
import java.util.*;

public class InMemoryFileDescriptor implements FileDescriptor {
  private final String name;
  private final String contents;
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
    return null;
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
}
