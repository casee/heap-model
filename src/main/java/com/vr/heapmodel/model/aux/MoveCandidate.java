package com.vr.heapmodel.model.aux;

import com.vr.heapmodel.model.Allocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MoveCandidate {

  private final Allocation allocation;
  private final int potential;
  private final boolean insideFreeSpace;

  public int getFrom() {
    return allocation.getFrom();
  }

  public int getSize() {
    return allocation.getSize();
  }

  public int getTo() {
    return allocation.getTo();
  }

  public int getAge() {
    return allocation.getAge();
  }

  public boolean isAvailable() {
    return allocation.isAvailable();
  }

}