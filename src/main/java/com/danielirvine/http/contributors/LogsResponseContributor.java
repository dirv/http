package com.danielirvine.http.contributors;

import java.util.*;

import com.danielirvine.http.*;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.HtmlHeadedContent;
import com.danielirvine.http.content.StringContent;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

public class LogsResponseContributor extends PathMatchingResponseContributor {

  private final Logger logger;

  public LogsResponseContributor(Logger logger) {
    super(asList("/logs"));
    this.logger = logger;
  }

  @Override
  public Response respond(Request request) {
    return new Response(ResponseCode.OK,
        new HtmlHeadedContent(logContent()));
  }

  private List<Content> logContent() {
    return logger.entries().stream().map(e->new StringContent(e.getRequestLine())).collect(toList());
  }
}
