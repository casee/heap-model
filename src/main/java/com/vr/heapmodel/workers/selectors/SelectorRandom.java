package com.vr.heapmodel.workers.selectors;

import com.vr.heapmodel.model.Item;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// выбираем случайный элемент
public class SelectorRandom implements Selector {

  private final Random random = new Random();

  public SelectorRandom() {
    log.info("selector = SelectorRandom");
  }

  @Override
  public Item choose(List<Item> items) {
    return items.get(random.nextInt(items.size()));
  }

}