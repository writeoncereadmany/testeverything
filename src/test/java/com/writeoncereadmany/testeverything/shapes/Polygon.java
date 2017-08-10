package com.writeoncereadmany.testeverything.shapes;

import java.util.List;
import java.util.Objects;

/**
 * Created by tomj on 01/08/2017.
 */
public class Polygon implements Shape {

    private final List<? extends Point> points;

    public Polygon(List<? extends Point> points) {
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
