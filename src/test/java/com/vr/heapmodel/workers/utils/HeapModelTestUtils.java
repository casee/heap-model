package com.vr.heapmodel.workers.utils;

import com.vr.heapmodel.model.Allocation;
import com.vr.heapmodel.model.Snapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vr.heapmodel.utils.HeapModelUtils.newItem;

public class HeapModelTestUtils {

    private static final Pattern ALLOCATION_PATTERN = Pattern.compile("([a-zA-Z]+)(\\d+)");

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