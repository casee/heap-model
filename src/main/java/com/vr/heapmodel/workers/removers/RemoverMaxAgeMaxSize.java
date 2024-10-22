package com.vr.heapmodel.workers.removers;

import static java.util.Comparator.comparing;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Snapshot;

// удаляем самый старый самый большой элемент
public class RemoverMaxAgeMaxSize implements Remover {

  public void remove(HeapApi api, Snapshot snapshot) {
    snapshot.getAllocations().stream()
        .filter(Allocation::isAvailable)
        .max(comparing(Allocation::getAge)
            .thenComparing(Allocation::getSize))
        .ifPresent(api::remove);
  }

}