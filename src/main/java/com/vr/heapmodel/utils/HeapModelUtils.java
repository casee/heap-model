package com.vr.heapmodel.utils;

import static lombok.AccessLevel.PRIVATE;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.FreeSpace;
import com.vr.heapmodel.model.Snapshot;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = PRIVATE)
public class HeapModelUtils {

  public static List<FreeSpace> findFreeSpaces(@NonNull Snapshot snapshot) {
    List<FreeSpace> freeSpaces = new ArrayList<>();

    int i = 0;
    for (Allocation allocation : snapshot.getAllocations()) {
      if (allocation.getFrom() > i) {
        freeSpaces.add(new FreeSpace(i, allocation.getFrom() - i));
      }
      i = allocation.getTo() + 1;
    }

    if (i < snapshot.getCapacity()) {
      freeSpaces.add(new FreeSpace(i, snapshot.getCapacity() - i));
    }

    return freeSpaces;
  }

}