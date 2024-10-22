package com.vr.heapmodel.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class FreeSpace {

  private final int position;
  private final int size;

}