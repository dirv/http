package com.danielirvine.http.sockets;

import java.io.*;

public interface ServerSocketProxy extends Closeable {
  SocketProxy accept() throws IOException;
  boolean hasData();
}
