package com.vr.heapmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HeapModelController {

  public static final int MAX_SPRINT_NUMBER = 100;

  private final HeapModelService service;

  public void run() {
    log.info("START");

    for (int i = 0; i < MAX_SPRINT_NUMBER; i++) {
      service.process();

      if (!service.hasActions()) {
        break;
      }
    }

    log.info("END");
  }

}