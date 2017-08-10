package com.writeoncereadmany.testeverything.examples;

import java.util.Objects;

public class Circle implements Shape {
    private final int radius;
    private final Point centre;

    public Circle(int radius, Point centre) {
        this.radius = radius;
        this.centre = centre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return radius == circle.radius &&
            Objects.equals(centre, circle.centre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius, centre);
    }
}
