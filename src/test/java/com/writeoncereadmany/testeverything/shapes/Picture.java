package com.writeoncereadmany.testeverything.shapes;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class Picture {

    private final List<Shape> shapes;

    public Picture(Shape ...shapes) {
        this.shapes = asList(shapes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(shapes, picture.shapes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shapes);
    }

    @Override
    public String toString() {
        return "Picture{" +
            "shapes=" + shapes +
            '}';
    }
}
