package com.vr.heapmodel.utils;

import static lombok.AccessLevel.PRIVATE;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.FreeSpace;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.model.MoveCandidate;
import com.vr.heapmodel.model.Snapshot;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = PRIVATE)
public class HeapModelUtils {

  public static Item newItem(int size) {
    return new Item(size, Character.toString(size + 96));
  }

  public static List<FreeSpace> toFreeSpaces(@NonNull Snapshot snapshot) {
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

  public static List<MoveCandidate> toMoveCandidates(List<Allocation> allocations, int capacity) {
    List<MoveCandidate> candidates = new ArrayList<>();

    for (int i = 0; i < allocations.size(); i++) {
      Allocation allocation = allocations.get(i);

      int potential = allocation.getSize();

      int leftNeighbour = i == 0 ? -1 : allocations.get(i - 1).getTo();
      int rightNeighbour = i == allocations.size() - 1 ? capacity : allocations.get(i + 1).getFrom();

      potential += allocation.getFrom() - leftNeighbour - 1;
      potential += rightNeighbour - allocation.getTo() - 1;

      candidates.add(new MoveCandidate(allocation, potential,
          isInsideFreeSpace(allocation, rightNeighbour, leftNeighbour)));
    }

    return candidates;
  }

  private static boolean isInsideFreeSpace(Allocation allocation, int rightNeighbour, int leftNeighbour) {
    return allocation.getFrom() > leftNeighbour + 1 && allocation.getTo() < rightNeighbour -1;
  }

}