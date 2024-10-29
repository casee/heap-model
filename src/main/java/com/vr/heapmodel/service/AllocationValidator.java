package com.vr.heapmodel.service;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.utils.MoveCounters;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.vr.heapmodel.HeapModelConstants.*;
import static com.vr.heapmodel.model.HeapAction.*;

@RequiredArgsConstructor
public class AllocationValidator {

    private final Heap heap;
    private final MoveCounters counters;

    public void canAllocate(Allocation allocation) {
        validateAllocateCounter();
        validateBounds(allocation);
        validateIntersection(allocation);
    }

    public void canRemove(Allocation allocation) {
        validateRemoveCounter();
        validateExists(allocation);
        validateIsAvailable(allocation);
    }

    public void canMove(Allocation allocation) {
        validateMoveCounter();
        validateBounds(allocation);
        validateIntersection(allocation);
    }


    private void validateExists(Allocation allocation) {
        if (!heap.contains(allocation)) {
            throw new IllegalArgumentException("Allocation is not in heap");
        }
    }

    private void validateIsAvailable(Allocation allocation) {
        if (!allocation.isAvailable()) {
            throw new IllegalArgumentException("Allocation cannot be removed before %s sprint".formatted(ALLOWED_AGE));
        }
    }

    private void validateBounds(Allocation allocation) {
        if (allocation.getFrom() < 0 || allocation.getTo() >= heap.getCapacity()) {
            throw new IllegalArgumentException("Allocation is out of bounds: %s".formatted(allocation));
        }
    }

    private void validateIntersection(Allocation allocation) {
        Optional<Allocation> intersection = heap.getAllocations().stream()
                .filter(a -> !a.equals(allocation))
                .filter(a -> isInside(a, allocation.getTo()) || isInside(a, allocation.getFrom()))
                .findAny();

        if (intersection.isPresent()) {
            throw new IllegalArgumentException("Allocation %s intersects with another one: %s"
                    .formatted(allocation, intersection.get()));
        }
    }

    private static boolean isInside(Allocation allocation, int position) {
        return position >= allocation.getFrom() && position <= allocation.getTo();
    }

    private void validateAllocateCounter() {
        if (counters.get(ALLOCATE) >= ALLOCATE_COUNT) {
            throw new IllegalArgumentException("ALLOCATE limit exceeded: " + counters.get(ALLOCATE));
        }
    }

    private void validateRemoveCounter() {
        if (counters.get(REMOVE) >= REMOVE_COUNT) {
            throw new IllegalArgumentException("REMOVE limit exceeded: " + counters.get(REMOVE));
        }
    }

    private void validateMoveCounter() {
        int count = counters.get(REMOVE) + counters.get(MOVE);
        if (count >= REMOVE_MOVE_COUNT) {
            throw new IllegalArgumentException("MOVE limit exceeded: " + count);
        }
    }

}