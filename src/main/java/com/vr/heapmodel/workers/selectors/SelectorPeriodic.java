package com.vr.heapmodel.workers.selectors;

import com.vr.heapmodel.model.Item;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// сортируем элементы по возрастанию размера
// выбираем элемент в зависимости от порядкового номера вставки по алгоритму:
//   0 - 0 - 0 - 1 - 1 - 1 - 2 - 2 - 2 - 0 - 0 - 0 - ...
public class SelectorPeriodic implements Selector {

  public static final int ALLOCATION_PERIOD = 3;

  private final AtomicInteger counter = new AtomicInteger();

  @Override
  public Item choose(List<Item> items) {
    ArrayList<Item> sortedItems = new ArrayList<>(items);
    sortedItems.sort(Comparator.comparing(Item::getSize).reversed());

    return sortedItems.get(periodicIndex(items.size(), ALLOCATION_PERIOD, counter.getAndIncrement()));
  }

  public static int periodicIndex(int bound, int period, int sprintCount) {
    return (sprintCount % (period * bound)) / period;
  }

}