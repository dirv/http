package com.danielirvine.http.resources;

import java.io.*;

import com.danielirvine.http.content.Content;

public interface Resource {
  Content toContent();
  void write(Reader in);
  void delete();
  long lastModified();
}
