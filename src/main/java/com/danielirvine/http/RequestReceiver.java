package com.danielirvine.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.danielirvine.http.responses.Response;
import com.danielirvine.http.sockets.SocketProxy;

class RequestReceiver implements Runnable {
  private final SocketProxy socket;
  private Logger logger;
  private Responder responder;

  RequestReceiver(Logger logger, Responder responder, SocketProxy socket) { 
    this.logger = logger;
    this.responder = responder;
    this.socket = socket;
  }

  public void run() {
    try {
      handleIncomingRequest(socket);
    } catch(Exception ex) {
    }
  }

  private void handleIncomingRequest(SocketProxy socket) throws IOException {
    try(InputStream in = socket.getInputStream()) {
      try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
        Request request = new Request(reader);
        logger.log(request);
        Response response = responder.response(request);
        try(BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream())) {
          response.write(output);
        }
      }
    }
    socket.close();
  }
}