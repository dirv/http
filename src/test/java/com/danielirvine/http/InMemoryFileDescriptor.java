package com.danielirvine.http;

import java.util.*;

public class InMemoryFileDescriptor implements FileDescriptor {
  private final String name;
  private final List<FileDescriptor> children;

  public InMemoryFileDescriptor(String name) {
    this(name, new ArrayList<FileDescriptor>());
  }

  public InMemoryFileDescriptor(String name, List<FileDescriptor> children) {
    this.name = name;
    this.children = children;
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
}
