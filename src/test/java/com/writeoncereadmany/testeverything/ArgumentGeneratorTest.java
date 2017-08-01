package com.writeoncereadmany.testeverything;

import com.writeoncereadmany.testeverything.examples.Circle;
import com.writeoncereadmany.testeverything.examples.Point;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ArgumentGeneratorTest {

    private final ArgumentGenerator argGen = new ArgumentGenerator();

    @Test
    public void canGenerateSensibleArgumentsForAPoint() throws Exception {
        Constructor<Point> constructor = Point.class.getConstructor(int.class, int.class);

        Object[] sampleArgs = argGen.sampleArgsFor(constructor);

        assertThat(sampleArgs, arrayContaining(instanceOf(Integer.class), instanceOf(Integer.class)));
    }

    @Test
    public void canGenerateSensibleArgumentsForACircle() throws Exception {
        Constructor<Circle> constructor = Circle.class.getConstructor(int.class, Point.class);

        Object[] sampleArgs = argGen.sampleArgsFor(constructor);

        assertThat(sampleArgs, arrayContaining(instanceOf(Integer.class), instanceOf(Point.class)));
    }


}