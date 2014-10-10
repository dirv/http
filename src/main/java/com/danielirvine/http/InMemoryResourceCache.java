package com.danielirvine.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.danielirvine.http.content.ByteArrayContent;
import com.danielirvine.http.content.Content;

public class InMemoryResourceCache {

  private static final int MAX_FILE_SIZE = 100000;
  private final Map<String, ByteArrayContent> contents = new HashMap<String, ByteArrayContent>();

  public void store(String path, Content content) {
    try {
      if(content.length() <= MAX_FILE_SIZE) {
        contents.put(path, convertToByteArrayContent(content));
      }
    } catch(IOException ex) {
    }
  }

  private ByteArrayContent convertToByteArrayContent(Content content) throws IOException {
    try(ByteArrayOutputStream str = new ByteArrayOutputStream()) {
      try(BufferedOutputStream out = new BufferedOutputStream(str)) {
        content.write(out);
        out.flush();
      }
      return new ByteArrayContent(str.toByteArray(), content.contentType());
    }
  }

  public boolean hasContent(String path) {
    return contents.containsKey(path);
  }
  
  
  public Content getContent(String path) {
    return contents.get(path);
  }

}
