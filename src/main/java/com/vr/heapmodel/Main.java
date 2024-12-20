package com.vr.heapmodel;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.api.HeapApiImpl;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.service.*;
import com.vr.heapmodel.utils.MoveCounters;
import com.vr.heapmodel.workers.Nagrebator;
import com.vr.heapmodel.workers.Razgrebator;
import com.vr.heapmodel.workers.allocators.Allocator;
import com.vr.heapmodel.workers.allocators.AllocatorLargestSpaceMiddle;
import com.vr.heapmodel.workers.relocators.Relocator;
import com.vr.heapmodel.workers.relocators.RelocatorMaxPotential;
import com.vr.heapmodel.workers.removers.Remover;
import com.vr.heapmodel.workers.removers.RemoverMaxAgeMaxSize;
import com.vr.heapmodel.workers.selectors.Selector;
import com.vr.heapmodel.workers.selectors.SelectorPeriodic;

import static com.vr.heapmodel.HeapModelConstants.HEAP_CAPACITY;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        Heap heap = new Heap(HEAP_CAPACITY);
        MoveCounters counters = new MoveCounters();

        HeapModelPrinter printer = new HeapModelPrinter();
        heap.registerListener(printer::printHeap);

        AllocationValidator validator = new AllocationValidator(heap, counters);
        HeapApi api = new HeapApiImpl(heap, validator);

        HeapModelService service = createService(heap, api, counters);
        heap.registerListener(service::onHeapChanged);

        new HeapModelController(service)
                .run();
    }

    private static HeapModelService createService(Heap heap, HeapApi api, MoveCounters counters) {
        Selector selector = new SelectorPeriodic();
        Allocator allocator = new AllocatorLargestSpaceMiddle();
        Nagrebator nagrebator = new Nagrebator(selector, allocator);

        Remover remover = new RemoverMaxAgeMaxSize();
        Relocator relocator = new RelocatorMaxPotential();
        Razgrebator razgrebator = new Razgrebator(remover, relocator);

        ItemsGenerator generator = new ItemsGenerator();

        return new HeapModelService(
                heap,
                api,
                generator,
                counters,
                nagrebator,
                razgrebator);
    }

}