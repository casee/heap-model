package com.vr.heapmodel.utils;

import com.vr.heapmodel.model.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

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

    public static List<MoveCandidate> toMoveCandidates(@NonNull Snapshot snapshot) {
        List<Allocation> allocations = snapshot.getAllocations();
        List<MoveCandidate> candidates = new ArrayList<>();

        for (int i = 0; i < allocations.size(); i++) {
            Allocation allocation = allocations.get(i);

            int potential = allocation.getSize();

            int leftNeighbour = i == 0 ? -1 : allocations.get(i - 1).getTo();
            int rightNeighbour = i == allocations.size() - 1 ? snapshot.getCapacity() : allocations.get(i + 1).getFrom();

            potential += allocation.getFrom() - leftNeighbour - 1;
            potential += rightNeighbour - allocation.getTo() - 1;

            candidates.add(new MoveCandidate(allocation, potential,
                    isInsideFreeSpace(allocation, rightNeighbour, leftNeighbour)));
        }

        return candidates;
    }

    private static boolean isInsideFreeSpace(Allocation allocation, int rightNeighbour, int leftNeighbour) {
        return allocation.getFrom() > leftNeighbour + 1 && allocation.getTo() < rightNeighbour - 1;
    }

}