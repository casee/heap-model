package com.vr.heapmodel.workers.allocators;

import static com.vr.heapmodel.utils.HeapModelUtils.toFreeSpaces;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.FreeSpace;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.model.Snapshot;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AllocatorMinSizeNearestSpace implements Allocator {

  @Override
  public void allocate(HeapApi api, Snapshot snapshot, Item item) {
    // находим свободные области
    List<FreeSpace> freeSpaces = toFreeSpaces(snapshot);

    // сортируем свободные области по возрастанию размера и позиции
    freeSpaces.sort(Comparator.comparing(FreeSpace::getSize)
        .thenComparing(FreeSpace::getFrom));

    // вставляем элемент в первую подходящую свободную область с минимальным размером
    for (FreeSpace freeSpace : freeSpaces) {
      if (freeSpace.getSize() >= item.getSize()) {
        api.allocate(item, freeSpace.getFrom());
        return;
      }
    }
  }

}