package com.writeoncereadmany.testeverything.someclasses;

import java.util.Objects;

public class WithEqualsOne {

    private final int number;

    public WithEqualsOne(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithEqualsOne that = (WithEqualsOne) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
