package com.danielirvine.http.contributors;

import java.util.ArrayList;
import java.util.List;

import com.danielirvine.http.*;
import com.danielirvine.http.content.*;
import com.danielirvine.http.ranges.*;
import com.danielirvine.http.resources.*;
import com.danielirvine.http.responses.*;

public class ResourceResponseContributor implements ResponseContributor {

  private final DirectoryResource root;
  private final InMemoryResourceCache cache;

  public ResourceResponseContributor(DirectoryResource root, InMemoryResourceCache cache) {
    this.root = root;
    this.cache = cache;
  }

  @Override
  public boolean canRespond(Request request) {
    return root.findResource(request.getPathSegments()) != null;
  }

  @Override
  public Response respond(Request request) {
    Content content = getContent(request);

    if(request.hasRanges()) {
      List<FixedRange> fixedRanges = fix(request.getRanges(), content.length());

      if(fixedRanges.size() == 0) {
        return new ErrorResponse(ResponseCode.UNSATISFIABLE);
      } else {
        return new Response(ResponseCode.PARTIAL, new MultiPartContent(content, fixedRanges));
      }
    }
    return new Response(ResponseCode.OK, content);
  }

  private Content getContent(Request request) {
    Content content;
    String path = request.getPath();
    if (cache.hasContent(path)) {
      content = cache.getContent(path);
    } else {
      Resource resource = root.findResource(request.getPath().split("/"));
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
