package com.vr.heapmodel.api;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.service.AllocationValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeapApiImpl implements HeapApi {

  private final Heap heap;
  private final AllocationValidator validator;

  @Override
  public void allocate(Item item, int position) {
    Allocation allocation = new Allocation(item, position);

    validator.canAllocate(allocation);

    heap.add(allocation);
  }

  @Override
  public void remove(Allocation allocation) {
    validator.canRemove(allocation);

    heap.remove(allocation);
  }

  @Override
  public void move(Allocation allocation, int position) {
    Allocation newAllocation = new Allocation(allocation.getItem(), allocation.getAge(),  position);

    validator.canMove(newAllocation);

    heap.replace(allocation, newAllocation);
  }

}