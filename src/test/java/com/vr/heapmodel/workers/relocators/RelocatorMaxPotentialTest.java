package com.vr.heapmodel.workers.relocators;

import static com.vr.heapmodel.utils.HeapModelUtils.newItem;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.vr.heapmodel.api.HeapApi;
import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Snapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelocatorMaxPotentialTest {

  @Mock
  private HeapApi api;

  private Relocator relocator;

  @BeforeEach
  void setUp() {
    relocator = new RelocatorMaxPotential();
  }

  @ParameterizedTest
  @MethodSource("movesAllocationParameters")
  void movesAllocation(String footprint, int expectedIndex, int position) {
    Snapshot snapshot = snapshotFromFootprint(footprint);

    relocator.move(api, snapshot, 1);

    Allocation expected = snapshot.getAllocations().get(expectedIndex);
    verify(api).move(expected, position);
  }

  private static Stream<Arguments> movesAllocationParameters() {
    return Stream.of(
        Arguments.of("|...|BB|ccc|ccc|ccc|..|",   3,  0),
        Arguments.of("|bb|ccc|.|BB|A|bb|bb|...|", 1, 13),
        Arguments.of("|CCC|BB|A|CCC|...|ccc|.|",  4,  9)
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