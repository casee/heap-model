package com.vr.heapmodel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeapModelConstants {

  public static final int HEAP_CAPACITY     = 16;
  public static final int MAX_SPRINT_ITEMS  = 3;
  public static final int ALLOWED_AGE       = 2;
  public static final int ALLOCATE_COUNT    = 2;
  public static final int REMOVE_MOVE_COUNT = 2;
  public static final int REMOVE_COUNT      = 1;

}