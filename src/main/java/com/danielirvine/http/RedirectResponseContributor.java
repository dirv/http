package com.danielirvine.http;

class RedirectResponseContributor implements ResponseContributor {

  private final UrlRedirects redirects;

  public RedirectResponseContributor(UrlRedirects redirects) {
    this.redirects = redirects;
  }

  @Override
  public boolean canRespond(Request request) {
    return redirects.hasRedirect(request.getPath());
  }

  @Override
  public Response respond(Request request) {
    return new ErrorResponse(ResponseCode.MOVED_PERMANENTLY,
          new ResponseHeader("Location", redirects.redirect(request.getPath())));
  }
}
