package com.vr.heapmodel.model;

import com.vr.heapmodel.HeapModelConstants;
import lombok.Data;

import java.util.Objects;

@Data
public class Allocation {

    private final Item item;
    private final int from;
    private final int to;

    private int age;

    public Allocation(Item item, int position) {
        this(item, 0, position);
    }

    public Allocation(Item item, int age, int position) {
        this.item = item;
        this.from = position;
        this.to = position + item.getSize() - 1;
        this.age = age;
    }

    public boolean isAvailable() {
        return age >= HeapModelConstants.ALLOWED_AGE;
    }

    public int getSize() {
        return item.getSize();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Allocation that = (Allocation) object;
        return item == that.item;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(item);
    }

}