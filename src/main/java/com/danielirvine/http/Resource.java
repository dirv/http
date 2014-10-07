package com.danielirvine.http;

import java.io.*;
import java.util.*;

interface Resource {
  ResponseCode getResponseCode();
  void dumpResource(PrintWriter out);
  List<Header> getHeaders();
}
