package com.danielirvine.http;

import org.junit.*;
import static org.junit.Assert.*;

public class ArgumentParserTest {
  @Test
	public void parsesIntFlagIfPresent() {
    ArgumentParser parser = new ArgumentParser(new String[]{"-p", "9090"});
    assertEquals(9090, parser.get("p", 5000));
  }

  @Test
  public void usesDefaultValueIfNonePresent() {
    ArgumentParser parser = new ArgumentParser(new String[]{""});
    assertEquals(5000, parser.get("p", 5000));
  }

  @Test
  public void parsesStringFlagIfPresent() {
    ArgumentParser parser = new ArgumentParser(new String[]{"-d", "blah"});
    assertEquals("blah", parser.get("d", "unknown"));
  }
}
