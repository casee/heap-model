package com.vr.heapmodel.workers.selectors;

import com.vr.heapmodel.model.Item;
import java.util.List;

public interface Selector {

  Item choose(List<Item> items);

}