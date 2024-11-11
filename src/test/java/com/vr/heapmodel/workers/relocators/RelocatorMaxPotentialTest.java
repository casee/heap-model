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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.vr.heapmodel.HeapModelConstants.HEAP_CAPACITY;
import static com.vr.heapmodel.service.HeapModelPrinter.snapshotToString;
import static com.vr.heapmodel.utils.HeapModelUtils.newItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class RelocatorMaxPotentialTest {

    private static final Pattern ALLOCATION_PATTERN = Pattern.compile("([a-zA-Z]+)(\\d+)");

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
        Snapshot snapshot = snapshotFromFootprint(beforeMove);
        heap.fromSnapshot(snapshot);

        relocator.move(api, snapshot, 1);
        String actual = snapshotToString(heap.getSnapshot());

        assertEquals(expected, actual);
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

    @Test
    void doesNotMoveAllocation() {
        Snapshot snapshot = snapshotFromFootprint("|bb1|....|BB3|A2|bb1|bb1|ccc1|");

        relocator.move(api, snapshot, 1);

        verifyNoInteractions(api);
    }


    public static Snapshot snapshotFromFootprint(String footprint) {
        List<Allocation> allocations = new ArrayList<>();

        String[] parts = footprint.split("\\|");

        int position = 0;
        for (String part : parts) {
            if (!part.isBlank()) {
                if (part.charAt(0) != '.') {
                    Allocation allocation = allocation(position, part);
                    allocations.add(allocation);
                    position += allocation.getSize();
                } else {
                    position += part.length();
                }
            }
        }

        return new Snapshot(position, allocations);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static Allocation allocation(int from, String part) {
        Matcher matcher = ALLOCATION_PATTERN.matcher(part);
        matcher.find();

        String symbols = matcher.group(1);
        int size = symbols.length();
        int age = Integer.parseInt(matcher.group(2));

        return new Allocation(newItem(size), age, from);
    }

}