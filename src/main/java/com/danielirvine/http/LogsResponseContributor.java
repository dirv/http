package com.danielirvine.http;

import java.util.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

class LogsResponseContributor extends PathMatchingResponseContributor {

  private final Logger logger;

  public LogsResponseContributor(Logger logger) {
    super(asList("/logs"));
    this.logger = logger;
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
