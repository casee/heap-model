package com.vr.heapmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HeapModelController {

  public static final int MAX_SPRINT_NUMBER = 100;

  private final HeapModelService service;

  private int noAllocationsCount = 0;

  public void run() {
    log.info("START");

    for (int i = 0; i < MAX_SPRINT_NUMBER; i++) {
      service.process();

      if (isNoAllocationsForTheLastTwoSprints()) {
        log.info("There were no allocations for the last two sprints! Sprints fulfilled: {}", i + 1);
        break;
      }
    }

    log.info("END");
  }

  private boolean isNoAllocationsForTheLastTwoSprints() {
    if (service.hasAllocations()) {
      noAllocationsCount = 0;
    } else {
      noAllocationsCount++;
    }

    return noAllocationsCount == 2;
  }

}