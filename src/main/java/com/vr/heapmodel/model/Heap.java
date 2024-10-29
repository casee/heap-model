package com.vr.heapmodel.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

import static com.vr.heapmodel.model.HeapAction.*;

@Getter
@RequiredArgsConstructor
public class Heap {

    private final int capacity;
    private final List<Allocation> allocations = new ArrayList<>();
    private final List<BiConsumer<Heap, HeapAction>> listeners = new ArrayList<>();

    public Snapshot getSnapshot() {
        return new Snapshot(capacity, List.copyOf(allocations));
    }

    public void add(Allocation allocation) {
        allocations.add(allocation);
        allocations.sort(Comparator.comparing(Allocation::getFrom));

        notifyListeners(ALLOCATE);
    }

    public void replace(Allocation allocation, Allocation newAllocation) {
        allocations.remove(allocation);
        allocations.add(newAllocation);
        allocations.sort(Comparator.comparing(Allocation::getFrom));

        notifyListeners(MOVE);
    }

    public void remove(Allocation allocation) {
        allocations.remove(allocation);

        notifyListeners(REMOVE);
    }

    public boolean contains(Allocation allocation) {
        return allocations.contains(allocation);
    }

    public void nextSprint() {
        allocations.forEach(a -> a.setAge(a.getAge() + 1));
    }

    public void registerListener(BiConsumer<Heap, HeapAction> listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners(HeapAction action) {
        listeners.forEach(a -> a.accept(this, action));
    }

}