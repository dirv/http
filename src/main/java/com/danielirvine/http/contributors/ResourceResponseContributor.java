package com.danielirvine.http.contributors;

import java.util.ArrayList;
import java.util.List;

import com.danielirvine.http.*;
import com.danielirvine.http.content.Content;
import com.danielirvine.http.content.MultiPartContent;
import com.danielirvine.http.ranges.FixedRange;
import com.danielirvine.http.ranges.Range;
import com.danielirvine.http.resources.DirectoryResource;
import com.danielirvine.http.resources.Resource;
import com.danielirvine.http.responses.ErrorResponse;
import com.danielirvine.http.responses.Response;
import com.danielirvine.http.responses.ResponseCode;

public class ResourceResponseContributor implements ResponseContributor {

  private final DirectoryResource root;

  public ResourceResponseContributor(DirectoryResource root) {
    this.root = root;
  }

  @Override
  public boolean canRespond(Request request) {
    return root.findResource(request.getPathSegments()) != null;
  }

  @Override
  public Response respond(Request request) {
    Resource resource = root.findResource(request.getPath().split("/"));
    Content content = resource.toContent();
    if(request.hasRanges()) {
      List<FixedRange> fixedRanges = fix(request.getRanges(), content.length());

      List<Content> partialContents = content.withRanges(fixedRanges);
      if(fixedRanges.size() == 0) {
        return new ErrorResponse(ResponseCode.UNSATISFIABLE);
      } else {
        return new Response(ResponseCode.PARTIAL, new MultiPartContent(fixedRanges, partialContents));
      }
    }
    return new Response(ResponseCode.OK, content);
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
