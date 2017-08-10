package com.writeoncereadmany.testeverything;

import com.writeoncereadmany.testeverything.someclasses.WithEqualsOne;
import com.writeoncereadmany.testeverything.someclasses.WithEqualsTwo;
import com.writeoncereadmany.testeverything.someclasses.WithoutEquals;
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
            "com.writeoncereadmany.testeverything.someclasses");

        assertThat(classes, containsInAnyOrder(
            WithEqualsOne.class,
            WithEqualsTwo.class,
            WithoutEquals.class
        ));
    }

    @Test
    public void canFindAllClassesWithMethod() throws Exception {
        List<Class<?>> classes = ClassFinder.findClasses(
            ClassFinderTest.class.getClassLoader(),
            "com.writeoncereadmany.testeverything.someclasses",
            implementing("equals", Object.class));

        assertThat(classes, containsInAnyOrder(
            WithEqualsOne.class,
            WithEqualsTwo.class
        ));
    }

}
