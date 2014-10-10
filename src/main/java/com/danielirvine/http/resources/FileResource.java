package com.danielirvine.http.resources;

import java.io.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.StreamContent;

public class FileResource implements Resource {

  private final FileDescriptor descriptor;

  public FileResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public Content toContent() {
    return new StreamContent(descriptor);
  }

  @Override
  public void write(Reader in) {
    descriptor.write(in);
  }

  @Override
  public void delete() {
    descriptor.delete();
  }
}
