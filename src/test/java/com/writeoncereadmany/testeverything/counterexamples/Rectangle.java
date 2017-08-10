package com.writeoncereadmany.testeverything.counterexamples;

import com.writeoncereadmany.testeverything.examples.Point;

import java.util.Objects;

public class Rectangle {

    private final Point first;
    private final Point second;
    private final Point third;

    public Rectangle(Point first, Point second, Point third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Objects.equals(first, rectangle.first) &&
            Objects.equals(second, rectangle.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
