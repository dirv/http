package com.danielirvine.http;

import static java.util.Arrays.asList;

import java.util.*;

class Responder {

  private final List<ResponseContributor> contributors;

  public Responder(Logger logger,
      List<String> writeablePaths,
      DirectoryResource root,
      UrlRedirects redirects,
      Authorizor authorizor) {
    this.contributors = asList(
          new UnauthorizedResponseContributor(authorizor),
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
