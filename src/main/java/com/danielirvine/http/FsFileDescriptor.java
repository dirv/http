package com.danielirvine.http;
import java.io.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

public class FsFileDescriptor implements FileDescriptor {

  private final File file;

  public FsFileDescriptor(File file) {
    this.file = file;
  }

  public String getName() {
    return file.getName();
  }

  public FileDescriptor getFile(String name) {
    File child = new File(file, name);
    return child.exists() ? new FsFileDescriptor(child) : null;
  }

  public InputStream getReadStream() {
    try {
      return new FileInputStream(file);
    } catch(FileNotFoundException ex) {
      return null;
    }
  }
}
