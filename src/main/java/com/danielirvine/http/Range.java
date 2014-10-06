package com.danielirvine.http;

import java.util.regex.*;
import java.util.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;
import java.util.stream.*;

public class Range {

  private static final Pattern headerPattern = Pattern.compile("bytes=([\\d\\-]*(?:,[\\d\\-]*)*)");
  private static final Pattern rangePattern = Pattern.compile("(?:(\\d+)\\-(\\d*))|(?:\\-(\\d*))");

  private List<RangeSpecifier> byteRanges = new ArrayList<RangeSpecifier>();
  private boolean hasError = false;

  public void processHeader(String header) {
    Matcher m = headerPattern.matcher(header);
    if (m.matches()) {
      processRanges(m.group(1).split(","));
    }
  }

  public boolean shouldIgnore() {
    return byteRanges.size() == 0 || hasError;
  }

  public List<FixedRangeSpecifier> fixForFileLength(long fileLength) {
    return byteRanges
      .stream()
      .map(r->r.fixForFileLength(fileLength))
      .filter(FixedRangeSpecifier::isSatisfiable)
      .collect(toList());
  }

  private void processRanges(String[] allRanges) {
    for(String range : allRanges) {
      processRange(range);
    }
    hasError = byteRanges.size() != allRanges.length;
  }

  private void processRange(String range) {
    Matcher rm = rangePattern.matcher(range);
    if(rm.matches()) {
      String start = rm.group(1);
      if(start == null) {
        processSuffixSize(rm.group(3));
      }
      else {
        processByteRange(start, rm.group(2));
      }
    }
  }

  private void processByteRange(String lowString, String highString) {
    long low = Long.parseLong(lowString);
    Long high = null;
    if(!highString.equals("")) {
      high = Long.parseLong(highString);
      if (high < low) {
        return;
      }
    }
    byteRanges.add(new ByteRange(low, high));
  }

  private void processSuffixSize(String suffixString) {
    long suffix = Long.parseLong(suffixString);
    byteRanges.add(new Suffix(suffix));
  }

  public interface RangeSpecifier {
    public FixedRangeSpecifier fixForFileLength(long fileLength);
  }

  private class ByteRange implements RangeSpecifier, FixedRangeSpecifier {
    private final long low;
    private final Long high;

    public ByteRange(long low, Long high) {
      this.low = low;
      this.high = high;
    }

    public FixedRangeSpecifier fixForFileLength(long fileLength) {
      if (high == null || high > fileLength) {
        return new ByteRange(low, fileLength);
      }
      return this;
    }

    public long getLow() {
      return low;
    }

    public long getHigh() {
      return high;
    }

    public boolean isSatisfiable() {
      return high >= low;
    }
  }

  private class Suffix implements RangeSpecifier {
    private final long length;

    public Suffix(long length) {
      this.length = length;
    }

    public FixedRangeSpecifier fixForFileLength(long fileLength) {
      return new ByteRange(fileLength-length, fileLength-1);
    }
  }
}
