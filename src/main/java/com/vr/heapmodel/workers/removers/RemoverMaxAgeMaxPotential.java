package com.vr.heapmodel.workers.removers;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.MoveCandidate;
import com.vr.heapmodel.model.Snapshot;
import lombok.extern.slf4j.Slf4j;

import static com.vr.heapmodel.utils.HeapModelUtils.toMoveCandidates;
import static java.util.Comparator.comparing;

@Slf4j
// удаляем самый старый элемент с наибольшим потенциалом (<размер> + <свободное место рядом>)
public class RemoverMaxAgeMaxPotential implements Remover {

    public RemoverMaxAgeMaxPotential() {
        log.info("remover = RemoverMaxPotential");
    }

    public void remove(HeapApi api, Snapshot snapshot) {
        toMoveCandidates(snapshot).stream()
                .filter(MoveCandidate::isAvailable)
                .sorted(comparing(MoveCandidate::getAge))
                .limit(2)
                .max(comparing(MoveCandidate::getPotential))
                .map(MoveCandidate::getAllocation)
                .ifPresent(api::remove);
    }

}