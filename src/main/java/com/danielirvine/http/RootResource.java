package com.danielirvine.http;

import java.io.*;

class RootResource implements Resource {

  public ResponseCode getResponseCode() {
    return ResponseCode.OK;
  }

  public void dumpResource(PrintWriter out) {
  }
}
