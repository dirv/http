package com.danielirvine.http;

import static java.util.Arrays.asList;

import java.util.*;

import com.danielirvine.http.contributors.*;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.responses.Response;


class Responder {

  private final List<ResponseContributor> contributors;

  public Responder(Logger logger,
      List<String> writeablePaths,
      DirectoryResource root,
      UrlRedirects redirects,
      Authorizer authorizer) {
    this.contributors = asList(
          new UnauthorizedResponseContributor(authorizer),
          new RedirectResponseContributor(redirects),
          new QueryResponseContributor(),
          new LogsResponseContributor(logger),
          new DeleteResponseContributor(root, writeablePaths),
          new PutPostResponseContributor(root, writeablePaths),
          new ResourceResponseContributor(root),
          new WriteableResponseContributor(writeablePaths),
          new NotFoundResponseContributor());
  }

  public Response response(Request request) {
    return contributors.stream()
      .filter(c->c.canRespond(request))
      .findFirst()
      .get()
      .respond(request);
  }
}
