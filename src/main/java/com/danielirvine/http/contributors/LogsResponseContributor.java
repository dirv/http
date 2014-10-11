package com.danielirvine.http.contributors;

import com.danielirvine.http.*;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.HtmlHeadedContent;
import com.danielirvine.http.content.ListContent;
import com.danielirvine.http.content.StringContent;
import com.danielirvine.http.responses.ContentResponse;
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
    return new ContentResponse(ResponseCode.OK,
        new HtmlHeadedContent(logContent()));
  }

  private Content logContent() {
    return new ListContent(logger.entries().stream().map(e->new StringContent(e.getRequestLine())).collect(toList()));
  }
}
