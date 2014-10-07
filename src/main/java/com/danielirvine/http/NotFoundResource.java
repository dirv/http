package com.danielirvine.http;

import java.io.*;
import java.util.*;
import static java.util.Arrays.*;


class NotFoundResource implements Resource {

  public ResponseCode getResponseCode() {
    return ResponseCode.NOT_FOUND;
  }

  public void dumpResource(Writer out) {
  }

  public List<ResponseHeader> getHeaders() {
    return asList();
  }

  public Resource applyRange(RangeHeader range) {
    return this;
  }
}
