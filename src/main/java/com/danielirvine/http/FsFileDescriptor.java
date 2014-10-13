package com.danielirvine.http;
import java.io.*;
import java.nio.file.*;

import static java.nio.file.StandardCopyOption.*;
import static java.util.stream.Stream.*;

import java.util.*;

import static java.util.stream.Collectors.*;
import static com.danielirvine.http.ExceptionWrapper.*;

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

  public FileDescriptor createFile(String name) {
    return getFile(name);
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

  public long length() {
    return file.length();
  }

  public String contentType() {
    return decheck(()->Files.probeContentType(file.toPath()));
  }

  public void write(Reader in) {
    decheck(()-> {
      File temp = File.createTempFile("http", ".tmp");
      try(BufferedWriter out = new BufferedWriter(new FileWriter(temp))){
        int currentByte;
        while((currentByte = in.read()) != -1) {
          out.write(currentByte);
        }
        out.write('\n');
      }
      return Files.move(temp.toPath(), file.toPath(), new CopyOption[]{REPLACE_EXISTING, ATOMIC_MOVE});
    });
  }

  public void delete() {
    file.delete();
  }

  private FileDescriptor getFile(File file) {
    return new FsFileDescriptor(file);
  }

  @Override
  public long lastModified() {
    return file.lastModified();
  }
}
