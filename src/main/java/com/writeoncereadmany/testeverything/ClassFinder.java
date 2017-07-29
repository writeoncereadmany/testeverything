package com.writeoncereadmany.testeverything;

import com.google.common.reflect.ClassPath;

import java.util.List;
import java.util.function.Predicate;

import static co.unruly.control.HigherOrderFunctions.compose;
import static java.util.stream.Collectors.toList;

public interface ClassFinder {

    static List<Class<?>> findClasses(ClassLoader classLoader, String basePackage, Predicate<Class<?>> ...additionalFilters) throws Exception {
        return ClassPath.from(classLoader)
            .getAllClasses()
            .stream()
            .filter(info -> info.getPackageName().startsWith(basePackage))
            .map(ClassPath.ClassInfo::load)
            .filter(compose(additionalFilters))
            .collect(toList());
    }
}
