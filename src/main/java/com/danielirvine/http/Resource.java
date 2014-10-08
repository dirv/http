package com.danielirvine.http;

import java.io.*;
import java.util.*;

interface Resource {
  Response toResponse();
  Resource applyRange(RangeHeader range);
}
