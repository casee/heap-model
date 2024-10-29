package com.vr.heapmodel.workers;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.workers.allocators.Allocator;
import com.vr.heapmodel.workers.selectors.Selector;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Nagrebator {

    private final Selector selector;
    private final Allocator allocator;

    public Item choose(List<Item> items) {
        return selector.choose(items);
    }

    public void allocate(HeapApi api, Snapshot snapshot, Item item) {
        allocator.allocate(api, snapshot, item);
    }

}