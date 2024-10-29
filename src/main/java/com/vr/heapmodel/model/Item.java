package com.vr.heapmodel.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Item {

    private final int size;
    private final String symbol;

}