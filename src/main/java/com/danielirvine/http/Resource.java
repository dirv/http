package com.danielirvine.http;

import java.io.*;

interface Resource {
  Response toResponse();
  Resource applyRange(RangeHeader range);
  void write(Reader in);
}
