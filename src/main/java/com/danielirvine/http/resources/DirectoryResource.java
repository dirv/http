package com.danielirvine.http.resources;

import java.io.*;
import java.util.*;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.HtmlHeadedContent;
import com.danielirvine.http.content.LinkContent;
import com.danielirvine.http.content.ListContent;
import com.danielirvine.http.ranges.Range;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

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
}
