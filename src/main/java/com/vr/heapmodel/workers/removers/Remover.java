package com.vr.heapmodel.workers.removers;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Snapshot;

public interface Remover {

    void remove(HeapApi api, Snapshot snapshot);

}