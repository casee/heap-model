package com.vr.heapmodel.api;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Item;

public interface HeapApi {

    void allocate(Item item, int position);

    void remove(Allocation allocation);

    void move(Allocation allocation, int position);

}