package com.danielirvine.http;

import java.io.*;

public interface Resource {
  Response toResponse();
  Resource applyRange(RangeHeader range);
  void write(Reader in);
  void delete();
}
