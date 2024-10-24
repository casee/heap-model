package com.vr.heapmodel.workers.relocators;

import static com.vr.heapmodel.utils.HeapModelUtils.toFreeSpaces;
import static com.vr.heapmodel.utils.HeapModelUtils.toMoveCandidates;
import static java.util.Comparator.comparing;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.model.aux.FreeSpace;
import com.vr.heapmodel.model.aux.MoveCandidate;
import java.util.List;

public class RelocatorMaxPotential implements Relocator {

  public void move(HeapApi api, Snapshot snapshot, int moveCount) {
    // находим свободные области
    List<FreeSpace> freeSpaces = toFreeSpaces(snapshot);

    if (freeSpaces.size() < 2) {
      return;
    }

    // сортируем кандидатов по убыванию потенциала и по убыванию позиции
    List<MoveCandidate> candidates = toMoveCandidates(snapshot.getAllocations(), snapshot.getCapacity()).stream()
        .sorted(comparing(MoveCandidate::getPotential)
            .thenComparing(MoveCandidate::getSize)
            .thenComparing(MoveCandidate::getFrom).reversed())
        .toList();

    // сортируем свободные области по возрастанию размера и по возрастанию позиции
    List<FreeSpace> availableFreeSpaces = freeSpaces.stream()
        .sorted(comparing(FreeSpace::getSize)
            .thenComparing(FreeSpace::getFrom))
        .toList();

    // перемещаем элемент с большим потенциалом в самую маленькую свободную область
    for (MoveCandidate candidate : candidates) {
      for (FreeSpace freeSpace : availableFreeSpaces) {
        if (isCandidateMatchFreeSpace(candidate, freeSpace)
            && (candidate.isInsideFreeSpace() || isCandidateFarFromFreeSpace(candidate, freeSpace))
            && isMovementEffective(candidate, freeSpace)
        ) {
          api.move(candidate.getAllocation(), freeSpace.getFrom());
          return;
        }
      }
    }
  }

  private boolean isCandidateMatchFreeSpace(MoveCandidate candidate, FreeSpace freeSpace) {
    return freeSpace.getSize() >= candidate.getSize();
  }

  private boolean isCandidateFarFromFreeSpace(MoveCandidate candidate, FreeSpace freeSpace) {
    return (freeSpace.getFrom() < candidate.getFrom() && candidate.getFrom() > freeSpace.getTo() + 1)
        || (freeSpace.getTo() > candidate.getTo() && candidate.getTo() < freeSpace.getFrom() - 1);
  }

  private boolean isMovementEffective(MoveCandidate candidate, FreeSpace freeSpace) {
    return candidate.getPotential() > freeSpace.getSize();
  }

}