package com.vr.heapmodel.workers.removers;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.MoveCandidate;
import com.vr.heapmodel.model.Snapshot;

import static com.vr.heapmodel.utils.HeapModelUtils.toMoveCandidates;
import static java.util.Comparator.comparing;

// удаляем самый старый самый большой элемент
public class RemoverMaxAgeMaxSize implements Remover {

    public void remove(HeapApi api, Snapshot snapshot) {
        toMoveCandidates(snapshot).stream()
                .filter(MoveCandidate::isAvailable)
                .sorted(comparing(MoveCandidate::getAge)
                        .thenComparing(MoveCandidate::getSize)
                        .reversed())
                .limit(2)
                .max(comparing(c -> ((MoveCandidate)c).getAge() + ((MoveCandidate)c).getSize())
                        .thenComparing(c -> ((MoveCandidate)c).getSize()))
                .map(MoveCandidate::getAllocation)
                .ifPresent(api::remove);
    }

}