package com.danielirvine.http;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.danielirvine.http.content.ByteArrayContent;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.resources.Resource;

public class InMemoryResourceCache {

  private static final int MAX_FILE_SIZE = 100000;
  private final ConcurrentMap<String, ByteArrayContent> contents = new ConcurrentHashMap<String, ByteArrayContent>();

  public void store(String path, Content content) {
    try {
      if(content.length() <= MAX_FILE_SIZE) {
        contents.put(path, ByteArrayContent.convert(content));
      }
    } catch(IOException ex) {
    }
  }

  public boolean hasCurrentContent(String path, Resource resource) {
    Content content = getContent(path);
    if(content != null) {
      return content.lastModified() == resource.lastModified();
    }
    return false;
  }
  
  public Content getContent(String path) {
    return contents.get(path);
  }
  
  public void deleteContent(String path) {
    contents.remove(path);
  }
}
