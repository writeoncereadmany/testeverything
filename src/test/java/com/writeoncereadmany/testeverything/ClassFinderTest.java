package com.writeoncereadmany.testeverything;

import com.writeoncereadmany.testeverything.examples.Book;
import com.writeoncereadmany.testeverything.examples.Circle;
import com.writeoncereadmany.testeverything.examples.Point;
import com.writeoncereadmany.testeverything.examples.Polygon;
import org.junit.Test;

import java.util.List;

import static com.writeoncereadmany.testeverything.ClassPredicates.implementing;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class ClassFinderTest {

    @Test
    public void canFindAllClassesInExamplePackage() throws Exception {
        List<Class<?>> classes = ClassFinder.findClasses(
            ClassFinderTest.class.getClassLoader(),
            "com.writeoncereadmany.testeverything.examples");

        assertThat(classes, containsInAnyOrder(
            Book.class,
            Circle.class,
            Point.class,
            Polygon.class
        ));
    }

    @Test
    public void canFindAllClassesWithMethod() throws Exception {
        List<Class<?>> classes = ClassFinder.findClasses(
            ClassFinderTest.class.getClassLoader(),
            "com.writeoncereadmany.testeverything.examples",
            implementing("equals", Object.class));

        assertThat(classes, containsInAnyOrder(
            Circle.class,
            Point.class,
            Polygon.class
        ));
    }

}