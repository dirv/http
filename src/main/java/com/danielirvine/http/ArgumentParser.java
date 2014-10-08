package com.danielirvine.http;

import java.lang.*;
import java.util.function.*;

public class ArgumentParser {

  private final String[] args;

  public ArgumentParser(String[] args) {
    this.args = args;
  }

  public int get(String flag, int defaultValue) {
    return get(flag, defaultValue, s -> Integer.parseInt(s));
  }

  public String get(String flag, String defaultValue) {
    return get(flag, defaultValue, s -> s);
  }

  private <T> T get(String flag, T defaultValue, Function<String, T> converter) {
    flag = "-" + flag;
    for(int i = 0; i < args.length - 1; ++i) {
      if(args[i].trim().equals(flag))
        return converter.apply(args[i+1].trim());
    }
    return defaultValue;
  }
}
