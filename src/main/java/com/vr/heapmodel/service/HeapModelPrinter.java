package com.vr.heapmodel.service;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.model.HeapAction;
import com.vr.heapmodel.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HeapModelPrinter {

  public void printHeap(Heap heap, HeapAction action) {
    int i = 0;
    List<String> parts = new ArrayList<>();
    for (Allocation allocation : heap.getAllocations()) {
      if (allocation.getFrom() > i) {
        parts.add(printFreeSpace(allocation.getFrom() - i));
      }
      parts.add(printAllocation(allocation));
      i = allocation.getTo() + 1;
    }

    if (i < heap.getCapacity()) {
      parts.add(printFreeSpace(heap.getCapacity() - i));
    }

    log.info("{}: {}", action.getSymbol(), printHeapParts(parts));
  }

  public static String printHeapParts(List<String> parts) {
    return parts.stream()
        .collect(Collectors.joining("|", "|", "|"));
  }

  public static String printFreeSpace(int size) {
    return ".".repeat(size);
  }

  public static String printAllocation(Allocation allocation) {
    Item item = allocation.getItem();
    String symbol = printItem(item);
    return allocation.isAvailable() ? symbol.toUpperCase() : symbol;
  }

  public static String printItem(Item item) {
    return item.getSymbol().repeat(item.getSize());
  }
}