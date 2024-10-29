package com.vr.heapmodel.workers.allocators;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.model.Snapshot;

public interface Allocator {

    void allocate(HeapApi api, Snapshot snapshot, Item item);

}