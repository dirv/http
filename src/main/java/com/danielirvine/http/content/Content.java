package com.danielirvine.http.content;

import java.io.*;
import java.util.*;

import com.danielirvine.http.headers.response.ContentTypeHeader;
import com.danielirvine.http.headers.response.ResponseHeader;
import com.danielirvine.http.ranges.FixedRange;

public interface Content {
  public void write(OutputStream out) throws IOException;
  public long length();
  public long lastModified();
  public ContentTypeHeader contentType();
  public List<Content> withRanges(List<FixedRange> range);
  public List<ResponseHeader> additionalHeaders();
}
