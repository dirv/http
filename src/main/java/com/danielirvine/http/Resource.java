package com.danielirvine.http;

import java.io.*;

interface Resource {
  ResponseCode getResponseCode();
  void dumpResource(PrintWriter out);
}
