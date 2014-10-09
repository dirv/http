package com.danielirvine.http.headers.request;

import java.util.regex.*;
import java.util.*;

import com.danielirvine.http.Request;
import com.danielirvine.http.ranges.*;

public class RangeHeader extends RequestHeader {

  private static final Pattern headerPattern = Pattern.compile("bytes=([\\d\\-]*(?:,[\\d\\-]*)*)");
  private static final Pattern rangePattern = Pattern.compile("(?:(\\d+)\\-(\\d*))|(?:\\-(\\d*))");

  private List<Range> ranges = new ArrayList<Range>();
  private boolean isValid = true;

  public RangeHeader(String header, Request request) {
    Matcher m = headerPattern.matcher(header);
    if (m.matches()) {
      processRanges(m.group(1).split(","));
      if(shouldApply()) {
        request.setRanges(ranges);
      }
    }
  }

  private boolean shouldApply() {
    return ranges.size() != 0 && isValid;
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
}
