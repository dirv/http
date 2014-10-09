package com.danielirvine.http;

import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.*;

class LogsContributor implements ResponseContributor {

  private final Logger logger;

  public LogsContributor(Logger logger) {
    this.logger = logger;
  }

  @Override
  public boolean canRespond(Request request) {
    return request.getPath().equals("/logs");
  }

  @Override
  public Response response(Request request) {
    return new Response(ResponseCode.OK,
        new PlainTextHeadedContent(logContent()));
  }

  private List<Content> logContent() {
    return logger.entries().stream().map(e->new StringContent(e.getRequestLine())).collect(toList());
  }
}
