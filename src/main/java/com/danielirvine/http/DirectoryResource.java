package com.danielirvine.http;

import java.util.*;
import java.util.stream.*;
import static java.util.Arrays.*;
import static java.util.stream.Stream.*;
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

  public Resource findResource(String[] pathSegments) {
    if(pathSegments.length == 0) {
      return this;
    } else {
      FileDescriptor child = descriptor.getFile(pathSegments[1]);
      if(child.exists()) {
        return new FileResource(child);
      } else {
        return new NotFoundResource();
      }
    }
  }

  public Resource applyRange(RangeHeader range) {
    return this;
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
