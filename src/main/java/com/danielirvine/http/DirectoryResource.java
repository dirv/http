package com.danielirvine.http;

import java.io.*;
import java.util.*;
import static java.util.stream.Collectors.*;

class DirectoryResource implements Resource {

  private final FileDescriptor descriptor;

  public DirectoryResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public Response toResponse() {
    return new Response(ResponseCode.OK,
        new PlainTextHeadedContent(generateLinks()));
  }

  public boolean hasResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return false;
    }
    FileDescriptor child = descriptor.getFile(pathSegments[1]);
    return child.exists();
  }

  public Resource findResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return this;
    } else {
      FileDescriptor child = descriptor.getFile(pathSegments[1]);
      if(child.exists()) {
        return new FileResource(child);
      } else {
        return null; // TODO - get rid of this call
      }
    }
  }

  public Resource findOrCreateResource(String[] pathSegments) {
    if (hasResource(pathSegments)) {
      return findResource(pathSegments);
    } else {
      return new FileResource(descriptor.createFile(pathSegments[1]));
    }
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }

  @Override
  public void write(Reader in) {
    return;
  }

  @Override
  public void delete() {
    return;
  }

  private List<Content> generateLinks() {
    return descriptor.getChildren()
      .stream()
      .map(this::createLink)
      .collect(toList());
  }

  private LinkContent createLink(FileDescriptor child) {
    String text = child.getName();
    return new LinkContent(text, text);
  }
}
