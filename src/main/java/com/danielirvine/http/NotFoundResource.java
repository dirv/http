package com.danielirvine.http;

import java.io.*;

class NotFoundResource implements Resource {

  public ResponseCode getResponseCode() {
    return ResponseCode.NOT_FOUND;
  }

  public void dumpResource(PrintWriter out) {
  }
}
