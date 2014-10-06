package com.danielirvine.http;

import java.io.*;

public interface ServerSocketProxy {
  SocketProxy accept() throws IOException;
}
