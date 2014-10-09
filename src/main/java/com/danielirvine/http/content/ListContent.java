package com.danielirvine.http.content;

import java.io.PrintStream;
import java.util.List;

public class ListContent implements Content {
  
  private List<Content> content;
  
  public ListContent(List<Content> content) {
    this.content = content;
  }

  @Override
  public void write(PrintStream out) {
    for(Content c : content) {
      c.write(out);
    }
    
  }

  @Override
  public long length() {
    return content.stream().mapToLong(s->s.length()).sum();
  }

}
