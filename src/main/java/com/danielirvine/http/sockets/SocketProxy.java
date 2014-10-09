package com.danielirvine.http.sockets;
import java.io.*;

public interface SocketProxy extends Closeable {
  InputStream getInputStream() throws IOException;
  OutputStream getOutputStream() throws IOException;
}
