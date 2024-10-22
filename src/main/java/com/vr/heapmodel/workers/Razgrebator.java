package com.vr.heapmodel.workers;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.workers.relocators.Relocator;
import com.vr.heapmodel.workers.removers.Remover;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Razgrebator {

  private final Remover remover;
  private final Relocator relocator;

  public void remove(HeapApi api, Snapshot snapshot) {
    remover.remove(api, snapshot);
  }

  public void move(HeapApi api, Snapshot snapshot, int moveCount) {
    relocator.move(api, snapshot, moveCount);
  }

}