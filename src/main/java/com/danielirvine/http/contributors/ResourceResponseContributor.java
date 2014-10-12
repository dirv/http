package com.danielirvine.http.contributors;

import java.util.ArrayList;
import java.util.List;

import com.danielirvine.http.*;
import com.danielirvine.http.content.*;
import com.danielirvine.http.ranges.*;
import com.danielirvine.http.resources.*;
import com.danielirvine.http.responses.*;

import static java.util.Arrays.*;

public class ResourceResponseContributor implements ResponseContributor {

  private final DirectoryResource root;
  private final InMemoryResourceCache cache;

  public ResourceResponseContributor(DirectoryResource root, InMemoryResourceCache cache) {
    this.root = root;
    this.cache = cache;
  }

  @Override
  public boolean canRespond(Request request) {
    return root.hasResource(request.getPathSegments());
  }

  @Override
  public Response respond(Request request) {
    Content content = getContent(request);

    if(request.hasRanges()) {
      List<FixedRange> fixedRanges = fix(request.getRanges(), content.length());

      if(fixedRanges.size() == 0) {
        return new ErrorResponse(ResponseCode.UNSATISFIABLE);
      } else {
        Content body = build(content, fixedRanges);
        return new ContentResponse(ResponseCode.PARTIAL, body);
      }
    }
    return new ContentResponse(ResponseCode.OK, content);
  }

  public static Content build(Content content, List<FixedRange> ranges) {
    List<Content> rangedContent = content.withRanges(ranges);
    if(rangedContent.size() == 1) {
      return new HeadedContent(asList(ranges.get(0).getHeader()), rangedContent.get(0));
    } else {
      return new MultiPartContent(rangedContent, ranges);
    }
  }

  private Content getContent(Request request) {
    Resource resource = root.findResource(request.getPathSegments());
    Content content;
    String path = request.getPath();
    if (cache.hasCurrentContent(path, resource)) {
      content = cache.getContent(path);
    } else {
      content = resource.toContent();
      cache.store(request.getPath(), content);
    }
    return content;
  }

  private List<FixedRange> fix(List<Range> ranges, long fileLength) {
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
}
