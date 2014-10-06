package com.danielirvine.http;
import java.io.*;

public interface SocketProxy {
  InputStream getInputStream() throws IOException;
  OutputStream getOutputStream() throws IOException;
}
