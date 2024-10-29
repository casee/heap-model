package com.vr.heapmodel.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Snapshot {

    private final int capacity;
    private final List<Allocation> allocations;

}