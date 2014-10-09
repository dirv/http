package com.danielirvine.http.resources;

import java.io.*;
import java.util.List;

import com.danielirvine.http.ranges.Range;
import com.danielirvine.http.responses.Response;

public interface Resource {
  Response toResponse();
  Resource applyRange(List<Range> range);
  void write(Reader in);
  void delete();
}
