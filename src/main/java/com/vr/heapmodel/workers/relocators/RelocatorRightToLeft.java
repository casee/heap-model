package com.vr.heapmodel.workers.relocators;

import static com.vr.heapmodel.utils.HeapModelUtils.findFreeSpaces;
import static java.util.Comparator.comparing;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.FreeSpace;
import com.vr.heapmodel.model.Snapshot;
import java.util.List;

public class RelocatorRightToLeft implements Relocator {

  public void move(HeapApi api, Snapshot snapshot, int moveCount) {
    // находим все свободные области
    List<FreeSpace> freeSpaces = findFreeSpaces(snapshot);

    if (freeSpaces.isEmpty()) {
      return;
    }

    // не рассматриваем элементы до первой свободной позиции - всегда переносим справа налево
    final int firstFreePosition = freeSpaces.get(0).getPosition();

    // список сортируем по убыванию размера и по убыванию позиции
    List<Allocation> allocations = snapshot.getAllocations().stream()
        .filter(a -> a.getFrom() > firstFreePosition)
        .sorted(comparing(Allocation::getSize)
            .thenComparing(Allocation::getFrom).reversed())
        .toList();

    // может не оказаться ни одного элемента дальше первого свободного пространства (все элементы слева)
    if (allocations.isEmpty()) {
      return;
    }

    // не рассматриваем самое правое свободное пространство
    // список сортируем по возрастанию размера и по возрастанию позиции
    List<FreeSpace> availableFreeSpaces = freeSpaces.stream()
        .filter(s -> s.getPosition() + s.getSize() < snapshot.getCapacity())
        .sorted(comparing(FreeSpace::getSize)
            .thenComparing(FreeSpace::getPosition))
        .toList();

    // перемещаем самый большой элемент в самое маленькое свободное пространство
    for (Allocation allocation : allocations) {
      for (FreeSpace freeSpace : availableFreeSpaces) {
        if (freeSpace.getSize() >= allocation.getSize()) {
          api.move(allocation, freeSpace.getPosition());
          return;
        }
      }
    }
  }

}