package com.danielirvine.http.resources;

import java.io.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.LinkContent;
import com.danielirvine.http.content.ListContent;

import static java.util.stream.Collectors.*;

public class DirectoryResource implements Resource {

  private final FileDescriptor descriptor;

  public DirectoryResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public Content toContent() {
    return new ListContent(descriptor.getChildren()
      .stream()
      .map(this::createLink)
      .collect(toList()));
  }

  public boolean hasResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return true;
    }
    FileDescriptor child = descriptor.getFile(pathSegments[1]);
    return child.exists();
  }

  public Resource findResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return this;
    } else {
      FileDescriptor child = descriptor.getFile(pathSegments[1]);
      return new FileResource(child);
    }
  }

  public Resource findOrCreateResource(String[] pathSegments) {
    if (hasResource(pathSegments)) {
      return findResource(pathSegments);
    } else {
      return new FileResource(descriptor.createFile(pathSegments[1]));
    }
  }

  @Override
  public void write(Reader in) {
    return;
  }

  @Override
  public void delete() {
    return;
  }

  private LinkContent createLink(FileDescriptor child) {
    String text = child.getName();
    return new LinkContent(text, text);
  }

  @Override
  public long lastModified() {
    return descriptor.lastModified();
  }

  @Override
  public String getETag() {
    // TODO Auto-generated method stub
    return null;
  }
}
