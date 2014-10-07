package com.danielirvine.http;

import java.io.*;
import java.util.*;

interface Resource {
  ResponseCode getResponseCode();
  void dumpResource(Writer out);
  List<ResponseHeader> getHeaders();
  Resource applyRange(RangeHeader range);
}
