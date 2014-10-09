package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import static java.util.Arrays.*;

public class UrlRedirectTest {

  @Test
	public void parsesSingleRedirect() {
    String[] redirect = new String[] {"/a => /b"};
    UrlRedirects r = new UrlRedirects(asList(redirect));
    assertEquals("/b", r.redirect("/a"));
  }

  @Test
	public void doesNotParseUnknownRedirect() {
    UrlRedirects r = new UrlRedirects(new ArrayList<String>());
    assertFalse(r.hasRedirect("/a"));
  }
}
