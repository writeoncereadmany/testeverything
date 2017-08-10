package com.writeoncereadmany.testeverything.examples;

public class Rectangle {

    private final Point topLeft;
    private final Point bottomRight;

    private Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public static Rectangle withLimits(int top, int left, int bottom, int right) {
        return new Rectangle(new Point(left, top), new Point(right, bottom));
    }
}
