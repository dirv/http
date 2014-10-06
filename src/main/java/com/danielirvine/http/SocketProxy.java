package com.danielirvine.http;
import java.io.*;

public interface SocketProxy extends Closeable {
  InputStream getInputStream() throws IOException;
  OutputStream getOutputStream() throws IOException;
}
