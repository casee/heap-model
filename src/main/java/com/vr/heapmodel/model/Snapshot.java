package com.vr.heapmodel.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Snapshot {

  private final int capacity;
  private final List<Allocation> allocations;

}