package com.danielirvine.http.resources;

import java.io.*;
import java.security.MessageDigest;

import com.danielirvine.http.FileDescriptor;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.StreamContent;

import static com.danielirvine.http.ExceptionWrapper.*;

public class FileResource implements Resource {

  private final FileDescriptor descriptor;

  public FileResource(FileDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  public Content toContent() {
    return new StreamContent(descriptor);
  }

  @Override
  public void write(Reader in) {
    descriptor.write(in);
  }

  @Override
  public void delete() {
    descriptor.delete();
  }

  @Override
  public long lastModified() {
    return descriptor.lastModified();
  }

  @Override
  public String getETag() {
    return decheck(() -> {
    MessageDigest md = MessageDigest.getInstance("SHA1");

    InputStream in = descriptor.getReadStream();
    byte[] dataBytes = new byte[1024];

    int nread = 0;

    while ((nread = in.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    };

    byte[] mdbytes = md.digest();

    StringBuffer sb = new StringBuffer("");
    for (int i = 0; i < mdbytes.length; i++) {
      sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
    });
  }
}
