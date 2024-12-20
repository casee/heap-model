package com.vr.heapmodel.service;

import com.vr.heapmodel.model.Item;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.vr.heapmodel.utils.HeapModelUtils.newItem;

public class ItemsGenerator {

    private final Random random = new Random();

    public List<Item> generate(int nItems) {
        return IntStream.range(0, nItems)
                .mapToObj(i -> newItem(random.nextInt(3) + 1))
                .toList();
    }

}