package com.vr.heapmodel.service;

import com.vr.heapmodel.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class HeapModelPrinter {

    public void printHeap(Heap heap, HeapAction action) {
        log.info("{}: {}", action.getSymbol(), snapshotToString(heap.getSnapshot()));
    }

    public static String snapshotToString(Snapshot snapshot) {
        int i = 0;
        List<String> parts = new ArrayList<>();
        for (Allocation allocation : snapshot.getAllocations()) {
            if (allocation.getFrom() > i) {
                parts.add(printFreeSpace(allocation.getFrom() - i));
            }
            parts.add(printAllocation(allocation));
            i = allocation.getTo() + 1;
        }

        if (i < snapshot.getCapacity()) {
            parts.add(printFreeSpace(snapshot.getCapacity() - i));
        }

        return printHeapParts(parts);
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
        return (allocation.isAvailable() ? symbol.toUpperCase() : symbol) + allocation.getAge();
    }

    public static String printItem(Item item) {
        return item.getSymbol().repeat(item.getSize());
    }

}