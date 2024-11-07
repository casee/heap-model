package com.vr.heapmodel.service;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.model.HeapAction;
import com.vr.heapmodel.model.Item;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.utils.MoveCounters;
import com.vr.heapmodel.workers.Nagrebator;
import com.vr.heapmodel.workers.Razgrebator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.vr.heapmodel.HeapModelConstants.*;
import static com.vr.heapmodel.model.HeapAction.*;

@Slf4j
@RequiredArgsConstructor
public class HeapModelService {

    private final Heap heap;
    private final HeapApi api;
    private final ItemsGenerator generator;
    private final MoveCounters counters;

    private final Nagrebator nagrebator;
    private final Razgrebator razgrebator;

    private final AtomicInteger sprintCount = new AtomicInteger(0);

    public void process() {
        newSprint();

        allocate();

        remove();

        while (moveCount() > 0) {
            int mc = moveCount();

            relocate();

            if (moveCount() == mc) {
                break;
            }
        }
    }

    public boolean hasAllocations() {
        return counters.get(ALLOCATE) > 0;
    }

    @SuppressWarnings("unused")
    public void onHeapChanged(Heap heap, HeapAction action) {
        counters.inc(action);
    }

    private void newSprint() {
        log.info("Sprint: {}", sprintCount.incrementAndGet());

        heap.nextSprint();
        counters.clear();
    }

    private void allocate() {
        List<Item> items = new ArrayList<>(generator.generate(MAX_SPRINT_ITEMS));

        for (int i = 0; i < ALLOCATE_COUNT; i++) {
            Item item = nagrebator.choose(items);

            printChosenItem(items, item);

            withApiAndSnapshot((a, s) -> nagrebator.allocate(a, s, item));

            items.remove(item);
        }
    }

    private void remove() {
        withApiAndSnapshot(razgrebator::remove);
    }

    private void relocate() {
        withApiAndSnapshot((a, s) -> razgrebator.move(a, s, moveCount()));
    }


    private int moveCount() {
        return REMOVE_MOVE_COUNT - counters.get(REMOVE) - counters.get(MOVE);
    }

    private void printChosenItem(List<Item> items, Item item) {
        log.info("    {} => {}",
                items.stream()
                        .map(HeapModelPrinter::printItem)
                        .collect(Collectors.joining(", ", "[", "]")),
                HeapModelPrinter.printItem(item)
        );
    }

    private void withApiAndSnapshot(BiConsumer<HeapApi, Snapshot> action) {
        action.accept(api, heap.getSnapshot());
    }

}