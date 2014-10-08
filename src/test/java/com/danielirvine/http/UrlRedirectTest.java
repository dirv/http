package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

public class UrlRedirectTest {

  @Test
	public void parsesSingleRedirect() {
    String redirect = "/a => /b";

    UrlRedirects r = new UrlRedirects(new StringBufferInputStream(redirect));
    assertEquals("/b", r.redirect("/a"));
  }

  @Test
	public void doesNotParseUnknownRedirect() {
    String redirect = "";
    UrlRedirects r = new UrlRedirects(new StringBufferInputStream(redirect));
    assertFalse(r.hasRedirect("/a"));
  }
}
