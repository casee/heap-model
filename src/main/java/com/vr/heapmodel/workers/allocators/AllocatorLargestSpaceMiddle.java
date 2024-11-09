package com.vr.heapmodel.workers.allocators;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.FreeSpace;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.model.Snapshot;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;

import static com.vr.heapmodel.utils.HeapModelUtils.toFreeSpaces;

@Slf4j
// вставляем в середину самой большой свободной области
public class AllocatorLargestSpaceMiddle implements Allocator {

    @Override
    public void allocate(HeapApi api, Snapshot snapshot, Item item) {
        toFreeSpaces(snapshot).stream()
                .filter(freeSpace -> freeSpace.getSize() >= item.getSize())
                .max(Comparator.comparing(FreeSpace::getSize))
                .ifPresent(freeSpace -> api.allocate(item,
                        freeSpace.getFrom() + (freeSpace.getSize() - item.getSize()) / 2
                ));
    }

}