package com.writeoncereadmany.testeverything.examples;

import java.util.function.Predicate;

public class ColorFilter {

    private final Predicate<Color> predicate;

    public ColorFilter(Predicate<Color> predicate) {
        this.predicate = predicate;
    }
}
