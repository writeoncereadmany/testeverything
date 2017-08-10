package com.writeoncereadmany.testeverything.someclasses;

import java.util.Objects;

public class WithEqualsTwo {

    private final int number;
    private final String text;

    public WithEqualsTwo(int number, String text) {
        this.number = number;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithEqualsTwo that = (WithEqualsTwo) o;
        return number == that.number &&
            Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, text);
    }
}
