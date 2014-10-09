package com.danielirvine.http.resources;

import java.io.*;

import com.danielirvine.http.Response;
import com.danielirvine.http.headers.request.RangeHeader;

public interface Resource {
  Response toResponse();
  Resource applyRange(RangeHeader range);
  void write(Reader in);
  void delete();
}
