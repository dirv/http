package com.danielirvine.http;
import java.io.*;
import java.util.*;

public interface FileDescriptor {
  public FileDescriptor getFile(String name);
  public String getName();
  public InputStream getReadStream();
  public List<FileDescriptor> getChildren();
  public long length();
  public boolean exists();
}
