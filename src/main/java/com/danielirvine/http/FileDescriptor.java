package com.danielirvine.http;
import java.io.*;
import java.util.*;

public interface FileDescriptor {
  public FileDescriptor getFile(String name);
  public FileDescriptor createFile(String name);
  public String getName();
  public InputStream getReadStream();
  public List<FileDescriptor> getChildren();
  public long length();
  public boolean exists();
  public String contentType();
  public void write(Reader in);
  public void delete();
  public long lastModified();
}