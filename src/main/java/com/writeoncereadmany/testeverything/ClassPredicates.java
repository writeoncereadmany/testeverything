package com.writeoncereadmany.testeverything;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface ClassPredicates {

    static Predicate<Class<?>> implementing(String methodName, Class<?>... parameterTypes) {
        return clazz -> Stream.of(clazz.getDeclaredMethods())
            .filter(m -> m.getName().equals(methodName))
            .anyMatch(m -> Arrays.equals(m.getParameterTypes(), parameterTypes));
    }
}
