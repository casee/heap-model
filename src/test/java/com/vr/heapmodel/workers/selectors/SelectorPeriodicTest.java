package com.vr.heapmodel.workers.selectors;

import static com.vr.heapmodel.workers.selectors.SelectorPeriodic.ALLOCATION_PERIOD;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vr.heapmodel.workers.selectors.SelectorPeriodic;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class SelectorPeriodicTest {

  private static final int ITEMS_SIZE = 3;

  @Test
  void generatesPeriodicIndices() {
    List<Integer> actual = IntStream.range(0, ITEMS_SIZE * ALLOCATION_PERIOD * 2)
        .map(i -> SelectorPeriodic.periodicIndex(ITEMS_SIZE, ALLOCATION_PERIOD, i))
        .boxed().toList();

    List<Integer> expected = List.of(0, 0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2);
    assertEquals(expected, actual);
  }

}