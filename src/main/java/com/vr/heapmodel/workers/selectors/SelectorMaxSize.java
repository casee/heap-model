package com.vr.heapmodel.workers.selectors;

import com.vr.heapmodel.model.Item;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
// выбираем элемент с самым большим размером
public class SelectorMaxSize implements Selector {

    public SelectorMaxSize() {
        log.info("selector = SelectorMaxSize");
    }

    @Override
    public Item choose(List<Item> items) {
        return items.stream()
                .max(Comparator.comparing(Item::getSize))
                .orElse(null);
    }

}