package com.danielirvine.http;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.StringContent;

public class InMemoryResourceCache {

  private static final int MAX_FILE_SIZE = 100000;
  private final Map<String, StringContent> contents = new HashMap<String, StringContent>();

  public void store(String path, Content content) {
    try {
      if(content.length() <= MAX_FILE_SIZE) {
        contents.put(path, convertToStringContent(content));
      }
    } catch(IOException ex) {
    }
  }

  private StringContent convertToStringContent(Content content) throws IOException {
    try(ByteArrayOutputStream str = new ByteArrayOutputStream()) {
      try(BufferedOutputStream out = new BufferedOutputStream(str)) {
        content.write(out);
        out.flush();
      }
      // TODO: probably needs to be a ByteArrayContent
      return new StringContent(str.toString());
    }
  }

  public boolean hasContent(String path) {
    return contents.containsKey(path);
  }
  
  
  public Content getContent(String path) {
    return contents.get(path);
  }

}
