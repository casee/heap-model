package com.vr.heapmodel.model.aux;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class FreeSpace {

  private final int from;
  private final int size;

  public int getTo() {
    return from + size - 1;
  }

}