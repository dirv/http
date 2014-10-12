package com.danielirvine.http;

import java.io.*;
import java.util.*;
import static com.danielirvine.http.ExceptionWrapper.*;

public class InMemoryFileDescriptor implements FileDescriptor {
  private String name;
  private String contents;
  private final List<FileDescriptor> children;
  private long lastModified;

  public InMemoryFileDescriptor(String name) {
    this(name, null);
  }

  public InMemoryFileDescriptor(String name, String contents) {
    this.name = name;
    this.children = new ArrayList<FileDescriptor>();
    this.contents = contents;
    lastModified = 1;
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

  public InMemoryFileDescriptor addFile(String name, String contents) {
    InMemoryFileDescriptor file = new InMemoryFileDescriptor(name, contents);
    children.add(file);
    return file;
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
    decheck(()-> {
      int currentByte;
      while ((currentByte = in.read()) != -1) {
        out.write(currentByte);
      }
      return null;
    });
    contents = out.toString(); 
    increaseLastModified();
  }

  private void increaseLastModified() {
    lastModified += 1;
  }

  public void delete() {
    name = "";
  }
  
  @Override
  public long lastModified() {
    return lastModified;
  }
}
