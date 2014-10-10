package com.danielirvine.http.resources;

import java.io.*;
import java.util.List;

import com.danielirvine.http.ranges.Range;
import com.danielirvine.http.responses.Response;

public interface Resource {
  Content toContent();
  void write(Reader in);
  void delete();
}
