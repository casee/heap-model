package com.vr.heapmodel.utils;

import com.vr.heapmodel.model.HeapAction;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MoveCounters {

  private final Map<HeapAction, AtomicInteger> counters;

  public MoveCounters() {
    counters = Arrays.stream(HeapAction.values())
        .collect(Collectors.toMap(a -> a, a -> new AtomicInteger(0)));
  }

  public int get(HeapAction action) {
    return counters.get(action).get();
  }

  public int sum() {
    return counters.values().stream().mapToInt(AtomicInteger::get).sum();
  }

  public void inc(HeapAction action) {
    counters.get(action).incrementAndGet();
  }

  public void add(HeapAction action, int delta) {
    counters.get(action).addAndGet(delta);
  }

  public void clear() {
    counters.values().forEach(a -> a.set(0));
  }

}