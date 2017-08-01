package com.writeoncereadmany.testeverything;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Generators;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class RecursiveConstructorGenerator implements Generators {

    @Override
    public <T> Generator<T> oneOf(Class<? extends T> first, Class<? extends T>... rest) {
        return null;
    }

    @Override
    public <T> Generator<T> oneOf(Generator<? extends T> first, Generator<? extends T>... rest) {
        return null;
    }

    @Override
    public Generator<?> field(Class<?> type, String fieldName) {
        return null;
    }

    @Override
    public <T> Generator<T> constructor(Class<T> type, Class<?>... argumentTypes) {
        return null;
    }

    @Override
    public <T> Generator<T> fieldsOf(Class<T> type) {
        return null;
    }

    @Override
    public <T> Generator<T> type(Class<T> type) {
        return null;
    }

    @Override
    public Generator<?> parameter(Parameter parameter) {
        return null;
    }

    @Override
    public Generator<?> field(Field field) {
        return null;
    }

    @Override
    public <T extends Generator<?>> T make(Class<T> genType, Generator<?>... componentGenerators) {
        return null;
    }
}
