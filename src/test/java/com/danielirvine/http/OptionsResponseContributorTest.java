package com.danielirvine.http;

import org.junit.*;

import com.danielirvine.http.contributors.OptionsResponseContributor;
import com.danielirvine.http.responses.Response;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;

public class OptionsResponseContributorTest extends RequestTest {

  private final List<String> writeables = Arrays.asList("/a");
  private final OptionsResponseContributor contributor = new OptionsResponseContributor(writeables);

  @Test
	public void respondToOptions() {
    startRequest("OPTIONS /a HTTP/1.1");
    assertTrue(contributor.canRespond(buildRequest()));
  }
  
  @Test
  public void returnsReadOnlyOptions() {
    startRequest("OPTIONS /read HTTP/1.1");
    Response response = contributor.respond(buildRequest());
    assertThat(responseText(response), hasItem(containsString("Allow: GET,HEAD,OPTIONS")));
  }
  
  @Test
  public void returnsWriteableOptions() {
    startRequest("OPTIONS /a HTTP/1.1");
    Response response = contributor.respond(buildRequest());
    assertThat(responseText(response), hasItem(containsString("Allow: GET,HEAD,POST,OPTIONS,PUT")));
  }
}
