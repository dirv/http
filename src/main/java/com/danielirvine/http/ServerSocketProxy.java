package com.danielirvine.http;

import java.io.*;

public interface ServerSocketProxy extends Closeable {
  SocketProxy accept() throws IOException;
}
