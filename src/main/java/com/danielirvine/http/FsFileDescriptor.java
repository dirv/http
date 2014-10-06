package com.danielirvine.http;
import java.io.*;
import static java.util.stream.Stream.*;
import java.util.*;
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
    return getFile(child);
  }

  public boolean exists() {
    return file.exists();
  }

  public InputStream getReadStream() {
    try {
      return new FileInputStream(file);
    } catch(FileNotFoundException ex) {
      return new ByteArrayInputStream(new byte[0]);
    }
  }

  public List<FileDescriptor> getChildren() {
    return of(file.listFiles())
      .filter(File::isFile)
      .map(this::getFile)
      .collect(toList());
  }

  private FileDescriptor getFile(File file) {
    return new FsFileDescriptor(file);
  }
}
