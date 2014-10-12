package com.danielirvine.http;

import java.util.concurrent.Callable;

public class ExceptionWrapper {
  public static <T> T decheck(Callable<T> callable) {
    try {
      return callable.call();
    }
    catch (RuntimeException e) {
      throw e;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}