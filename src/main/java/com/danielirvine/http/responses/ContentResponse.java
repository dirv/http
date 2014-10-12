
package com.danielirvine.http.responses;

import java.io.*;
import java.util.*;

import com.danielirvine.http.content.Content;
import com.danielirvine.http.headers.response.ContentLengthHeader;
import com.danielirvine.http.headers.response.ResponseHeader;

public class ContentResponse extends Response {

  private final Content body;

  public ContentResponse(ResponseCode code, Content body) {
    super(code, getHeaders(body)); 
    this.body = body;
  }

  private static List<ResponseHeader> getHeaders(Content body) {
    List<ResponseHeader> headers = new ArrayList<ResponseHeader>(body.additionalHeaders());
    headers.add(body.contentType());
    headers.add(new ContentLengthHeader(body.length()));
    return headers;
  }

  public void write(OutputStream out) throws IOException {
    super.write(out);
    body.write(out);
  }
}
