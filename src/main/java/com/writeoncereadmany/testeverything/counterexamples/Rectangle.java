package com.writeoncereadmany.testeverything.counterexamples;

import java.util.Objects;

public class Rectangle {

    private final int left;
    private final int right;
    private final int top;
    private final int bottom;

    public Rectangle(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return left == rectangle.left &&
            right == rectangle.right &&
            bottom == rectangle.bottom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, bottom);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
            "left=" + left +
            ", right=" + right +
            ", top=" + top +
            ", bottom=" + bottom +
            '}';
    }
}
