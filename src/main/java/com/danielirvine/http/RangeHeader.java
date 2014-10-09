package com.danielirvine.http;

import java.util.regex.*;
import java.util.*;

public class RangeHeader extends RequestHeader {

  private static final Pattern headerPattern = Pattern.compile("bytes=([\\d\\-]*(?:,[\\d\\-]*)*)");
  private static final Pattern rangePattern = Pattern.compile("(?:(\\d+)\\-(\\d*))|(?:\\-(\\d*))");

  private List<Range> ranges = new ArrayList<Range>();
  private boolean isValid = true;

  public RangeHeader(String header) {
    Matcher m = headerPattern.matcher(header);
    if (m.matches()) {
      processRanges(m.group(1).split(","));
    }
  }

  public Resource apply(Resource resource) {
    return shouldApply() ? resource.applyRange(this) : resource;
  }

  private boolean shouldApply() {
    return ranges.size() != 0 && isValid;
  }

  public List<FixedRange> fix(long fileLength) {
    long curPos = 0;

    List<FixedRange> specifiers = new ArrayList<FixedRange>();
    for(Range s : ranges) {
      FixedRange specifier = s.fix(curPos, fileLength);
      if(specifier.isSatisfiable()) {
        curPos += specifier.length() + 1;
        specifiers.add(specifier);
      }
    }
    return specifiers;
  }

  private void processRanges(String[] allRanges) {
    for(String range : allRanges) {
      processRange(range);
    }
    isValid = ranges.size() == allRanges.length;
  }

  private void processRange(String range) {
    Matcher rm = rangePattern.matcher(range);
    if(rm.matches()) {
      String start = rm.group(1);
      if(start == null) {
        processSuffixRangeSize(rm.group(3));
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
    ranges.add(new ByteRange(low, high));
  }

  private void processSuffixRangeSize(String suffixString) {
    long suffix = Long.parseLong(suffixString);
    ranges.add(new SuffixRange(suffix));
  }

  public interface Range {
    public FixedRange fix(long previousPosition, long fileLength);
  }

  private class ByteRange implements Range {
    private final long low;
    private final Long high;

    public ByteRange(long low, Long high) {
      this.low = low;
      this.high = high;
    }

    public FixedRange fix(long previousPosition, long fileLength) {
      if (high == null || high > fileLength) {
        return new FixedRange(previousPosition, low, fileLength-1, fileLength);
      }
      return new FixedRange(previousPosition, low, high, fileLength);
    }
  }

  private class SuffixRange implements Range {
    private final long length;

    public SuffixRange(long length) {
      this.length = length;
    }

    public FixedRange fix(long previousPosition, long fileLength) {
      long low = fileLength - length;
      if (low < 0) low = 0;
      return new FixedRange(previousPosition, low, fileLength-1, fileLength);
    }
  }
}
