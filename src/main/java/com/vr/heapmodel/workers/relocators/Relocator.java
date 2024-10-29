package com.vr.heapmodel.workers.relocators;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Snapshot;

public interface Relocator {

    void move(HeapApi api, Snapshot snapshot, int moveCount);

}