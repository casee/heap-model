package com.vr.heapmodel.workers.removers;

import static com.vr.heapmodel.utils.HeapModelUtils.toMoveCandidates;
import static java.util.Comparator.comparing;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.model.aux.MoveCandidate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// удаляем самый старый элемент с наибольшим потенциалом (<размер> + <свободное место рядом>)
public class RemoverMaxAgeMaxPotential implements Remover {

  public RemoverMaxAgeMaxPotential() {
    log.info("remover = RemoverMaxPotential");
  }

  public void remove(HeapApi api, Snapshot snapshot) {
    toMoveCandidates(snapshot.getAllocations(), snapshot.getCapacity()).stream()
        .filter(MoveCandidate::isAvailable)
        .max(comparing(MoveCandidate::getAge)
            .thenComparing(MoveCandidate::getPotential))
        .map(MoveCandidate::getAllocation)
        .ifPresent(api::remove);
  }

}