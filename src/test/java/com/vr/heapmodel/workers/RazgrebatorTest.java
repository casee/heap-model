package com.vr.heapmodel.workers;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.api.HeapApiImpl;
import com.vr.heapmodel.model.Heap;
import com.vr.heapmodel.model.Snapshot;
import com.vr.heapmodel.service.AllocationValidator;
import com.vr.heapmodel.workers.relocators.Relocator;
import com.vr.heapmodel.workers.relocators.RelocatorMaxPotential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static com.vr.heapmodel.HeapModelConstants.HEAP_CAPACITY;
import static com.vr.heapmodel.service.HeapModelPrinter.snapshotToString;
import static com.vr.heapmodel.workers.utils.HeapModelTestUtils.snapshotFromFootprint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RazgrebatorTest {

    @Mock
    private AllocationValidator validator;

    private Heap heap;
    private HeapApi api;
    private Relocator relocator;

    @BeforeEach
    void setUp() {
        heap = new Heap(HEAP_CAPACITY);
        api = spy(new HeapApiImpl(heap, validator));
        relocator = new RelocatorMaxPotential();
    }

    @ParameterizedTest
    @MethodSource("movesAllocationParameters")
    void movesAllocation(String beforeMove, String expected) {
        actionTest((a, s) -> relocator.move(a, s, 1), beforeMove, expected);
    }

    private static Stream<Arguments> movesAllocationParameters() {
        return Stream.of(
                Arguments.of("|...|BB2|ccc1|ccc1|ccc1|..|",          "|ccc1|BB2|ccc1|ccc1|.....|"),
                Arguments.of("|bb1|ccc1|.|BB3|A2|bb1|bb1|...|",      "|bb1|....|BB3|A2|bb1|bb1|ccc1|"),
                Arguments.of("|CCC3|BB3|A2|CCC3|...|ccc1|.|",        "|CCC3|BB3|A2|CCC3|....|ccc1|"),
                Arguments.of("|BB3|bb1|...|BB3|CCC3|.|BB3|.|",       "|BB3|bb1|CCC3|BB3|....|BB3|.|"),
                Arguments.of("|A2|A2|.|BB3|...|A2|bb1|a1|BB3|BB3|",  "|A2|A2|A2|BB3|....|bb1|a1|BB3|BB3|"),
                Arguments.of("|......|ccc1|..|bb1|...|",             "|...........|bb1|ccc1|"),
                Arguments.of("|...|CCC3|a1|ccc1|..|bb1|..|",         "|ccc1|CCC3|a1|.....|bb1|..|")
        );
    }


    private void actionTest(BiConsumer<HeapApi, Snapshot> action, String beforeMove, String expected) {
        Snapshot snapshot = snapshotFromFootprint(beforeMove);
        heap.fromSnapshot(snapshot);

        action.accept(api, snapshot);
        String actual = snapshotToString(heap.getSnapshot());

        assertEquals(expected, actual);
    }

    @Test
    void doesNotMoveAllocation() {
        Snapshot snapshot = snapshotFromFootprint("|bb1|....|BB3|A2|bb1|bb1|ccc1|");

        relocator.move(api, snapshot, 1);

        verifyNoInteractions(api);
    }

}