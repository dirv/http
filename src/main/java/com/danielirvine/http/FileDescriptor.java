package com.danielirvine.http;
import java.io.*;

public interface FileDescriptor {
  public FileDescriptor getFile(String name);
  public String getName();
  public InputStream getReadStream();
}
