package com.vr.heapmodel.workers.relocators;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.api.HeapApiImpl;
import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.service.AllocationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.vr.heapmodel.HeapModelConstants.HEAP_CAPACITY;
import static com.vr.heapmodel.service.HeapModelPrinter.snapshotToString;
import static com.vr.heapmodel.utils.HeapModelUtils.newItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RelocatorMaxPotentialTest {

    @Mock
    private AllocationValidator validator;

    private Heap heap;
    private HeapApi api;
    private Relocator relocator;

    @BeforeEach
    void setUp() {
        heap = new Heap(HEAP_CAPACITY);
        api = new HeapApiImpl(heap, validator);
        relocator = new RelocatorMaxPotential();
    }

    @ParameterizedTest
    @MethodSource("movesAllocationParameters")
    void movesAllocation(String beforeMove, String expected) {
        Snapshot snapshot = snapshotFromFootprint(beforeMove);
        heap.fromSnapshot(snapshot);

        relocator.move(api, snapshot, 1);
        String actual = snapshotToString(heap.getSnapshot());

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> movesAllocationParameters() {
        return Stream.of(
                Arguments.of("|...|BB|ccc|ccc|ccc|..|",     "|ccc|BB|ccc|ccc|.....|"),
                Arguments.of("|bb|ccc|.|BB|A|bb|bb|...|",   "|bb|....|BB|A|bb|bb|ccc|"),
                Arguments.of("|CCC|BB|A|CCC|...|ccc|.|",    "|CCC|BB|A|CCC|....|ccc|"),
                Arguments.of("|BB|bb|...|BB|CCC|.|BB|.|",   "|BB|bb|CCC|BB|....|BB|.|"),
                Arguments.of("|A|A|.|BB|...|A|bb|a|BB|BB|", "|A|A|A|BB|....|bb|a|BB|BB|"),
                Arguments.of("|......|ccc|..|bb|...|",      "|...........|bb|ccc|")
        );
    }

    @Test
    void doesNotMoveAllocation() {
        Snapshot snapshot = snapshotFromFootprint("|bb|....|BB|A|bb|bb|ccc|");

        relocator.move(api, snapshot, 1);

        verifyNoInteractions(api);
    }


    private static Allocation allocation(int from, int size, int age) {
        return new Allocation(newItem(size), age, from);
    }

    private static Snapshot snapshotFromFootprint(String footprint) {
        List<Allocation> allocations = new ArrayList<>();

        String[] parts = footprint.split("\\|");

        int position = 0;
        for (String part : parts) {
            if (!part.isBlank()) {
                if (part.charAt(0) != '.') {
                    Allocation allocation = allocation(position, part.length(), part.equals(part.toUpperCase()) ? 3 : 1);
                    allocations.add(allocation);
                }
                position += part.length();
            }
        }

        return new Snapshot(position, allocations);
    }

}