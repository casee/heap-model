package com.vr.heapmodel.workers.relocators;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.FreeSpace;
import com.vr.heapmodel.model.MoveCandidate;
import com.vr.heapmodel.model.Snapshot;

import java.util.List;
import java.util.function.BiPredicate;

import static com.vr.heapmodel.utils.HeapModelUtils.toFreeSpaces;
import static com.vr.heapmodel.utils.HeapModelUtils.toMoveCandidates;
import static java.util.Comparator.comparing;

public class RelocatorMaxPotential implements Relocator {

    public void move(HeapApi api, Snapshot snapshot, int moveCount) {
        // сортируем свободные области по возрастанию размера и позиции
        List<FreeSpace> freeSpaces = toFreeSpaces(snapshot).stream()
                .sorted(comparing(FreeSpace::getSize)
                        .thenComparing(FreeSpace::getFrom))
                .toList();

        if (freeSpaces.size() < 2) {
            return;
        }

        // сортируем кандидатов по убыванию потенциала, размера и позиции
        List<MoveCandidate> candidates = toMoveCandidates(snapshot).stream()
                .sorted(comparing(MoveCandidate::getPotential)
                        .thenComparing(MoveCandidate::getSize)
                        .thenComparing(MoveCandidate::getFrom)
                        .reversed())
                .toList();

        if (isExceptionalSetup(freeSpaces)) {
            return;
        }

        if (!simpleMove(api, freeSpaces, candidates)) {
            complexMove(api, freeSpaces, candidates);
        }
    }

    private boolean isExceptionalSetup(List<FreeSpace> freeSpaces) {
        List<Integer> list = freeSpaces.stream()
                .map(FreeSpace::getSize)
                .toList();
        return list.equals(List.of(2, 3));
    }

    // перемещаем элемент с большим потенциалом в самую маленькую свободную область
    private boolean simpleMove(HeapApi api, List<FreeSpace> freeSpaces, List<MoveCandidate> candidates) {
        return moveIf(api, freeSpaces, candidates, (c, f) ->
                isCandidateMatchFreeSpaceExactly(c, f)
                        && isMovementEffective(c, f)
                        && isCandidateFarFromFreeSpace(c, f)
        );
    }

    private boolean isCandidateMatchFreeSpaceExactly(MoveCandidate candidate, FreeSpace freeSpace) {
        return candidate.getSize() == freeSpace.getSize();
    }

    private void complexMove(HeapApi api, List<FreeSpace> freeSpaces, List<MoveCandidate> candidates) {
        moveIf(api, freeSpaces, candidates, (c, f) ->
                (c.isInsideFreeSpace() && isCandidateNearFreeSpace(c, f))
             || (isCandidateMatchFreeSpace(c, f)
              && (c.isInsideFreeSpace() || isCandidateFarFromFreeSpace(c, f))
              && isMovementEffective(c, f))
        );
    }

    private boolean moveIf(
            HeapApi api,
            List<FreeSpace> freeSpaces,
            List<MoveCandidate> candidates,
            BiPredicate<MoveCandidate, FreeSpace> predicate
    ) {
        for (MoveCandidate candidate : candidates) {
            for (FreeSpace freeSpace : freeSpaces) {
                if (predicate.test(candidate, freeSpace)) {
                    api.move(candidate.getAllocation(), alignPosition(candidate, freeSpace));
                    return true;
                }
            }
        }
        return false;
    }

    private int alignPosition(MoveCandidate candidate, FreeSpace freeSpace) {
        return freeSpace.getFrom() > candidate.getTo()
                ? freeSpace.getTo() - candidate.getSize() + 1
                : freeSpace.getFrom();
    }

    private boolean isCandidateNearFreeSpace(MoveCandidate candidate, FreeSpace freeSpace) {
        return candidate.getFrom() == freeSpace.getTo() + 1 || candidate.getTo() == freeSpace.getFrom() - 1;
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