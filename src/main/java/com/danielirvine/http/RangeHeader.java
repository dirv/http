package com.danielirvine.http;

import java.util.regex.*;
import java.util.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;
import java.util.stream.*;

public class RangeHeader implements RequestHeader {

  private static final Pattern headerPattern = Pattern.compile("bytes=([\\d\\-]*(?:,[\\d\\-]*)*)");
  private static final Pattern rangePattern = Pattern.compile("(?:(\\d+)\\-(\\d*))|(?:\\-(\\d*))");

  private List<RangeSpecifier> byteRanges = new ArrayList<RangeSpecifier>();
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
    return byteRanges.size() != 0 && isValid;
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
    isValid = byteRanges.size() == allRanges.length;
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
    private final long fileLength;

    public ByteRange(long low, Long high) {
      this(low, high, 0);
    }

    public ByteRange(long low, Long high, long fileLength) {
      this.low = low;
      this.high = high;
      this.fileLength = fileLength;
    }

    public FixedRangeSpecifier fixForFileLength(long fileLength) {
      if (high == null || high > fileLength) {
        return new ByteRange(low, fileLength-1, fileLength);
      }
      return new ByteRange(low, high, fileLength);
    }

    public long getLow() {
      return low;
    }

    public long getHigh() {
      return high;
    }

    public long length() {
      return high - low + 1;
    }

    public boolean isSatisfiable() {
      return high >= low;
    }

    public Header getContentRangeHeader() {
      return new Header() {
        @Override
        public String toString() {
          return "Content-range: bytes " + low + "-" + high + "/" + fileLength;
        }
      };
    }
  }

  private class Suffix implements RangeSpecifier {
    private final long length;

    public Suffix(long length) {
      this.length = length;
    }

    public FixedRangeSpecifier fixForFileLength(long fileLength) {
      long low = fileLength - length;
      if (low < 0) low = 0;
      return new ByteRange(low, fileLength-1, fileLength);
    }
  }
}
