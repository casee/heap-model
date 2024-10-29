package com.vr.heapmodel.model;

import lombok.Getter;

@Getter
public enum HeapAction {

    ALLOCATE("++"),
    REMOVE("--"),
    MOVE("<<");

    private final String symbol;

    HeapAction(String symbol) {
        this.symbol = symbol;
    }

}