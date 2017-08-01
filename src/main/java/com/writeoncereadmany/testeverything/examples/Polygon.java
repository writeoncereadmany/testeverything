package com.writeoncereadmany.testeverything.examples;

import java.util.List;
import java.util.Objects;

/**
 * Created by tomj on 01/08/2017.
 */
public class Polygon {

    private final List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon polygon = (Polygon) o;
        return Objects.equals(points, polygon.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }
}
