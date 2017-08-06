package com.writeoncereadmany.testeverything;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.writeoncereadmany.testeverything.examples.Circle;
import com.writeoncereadmany.testeverything.examples.Point;
import com.writeoncereadmany.testeverything.examples.Polygon;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ArgumentGeneratorTest {

    private final SourceOfRandomness source = new SourceOfRandomness(new Random());
    private final SimpleGenerationStatus status = new SimpleGenerationStatus(new GeometricDistribution(), source, 10);
    private final ArgumentGenerator argGen = new ArgumentGenerator();

    @Test
    public void canCreateInstancesOfCustomClasses() {
        Generator<?> generator = argGen.generatorFor(Point.class);

        Object output = generator.generate(source, status);

        assertThat(output, instanceOf(Point.class));
    }

    @Test
    public void canCreateInstancesOfCustomClassesWithCustomArguments() {
        Generator<?> generator = argGen.generatorFor(Circle.class);

        Object output = generator.generate(source, status);

        assertThat(output, instanceOf(Circle.class));
    }

    @Test
    public void canCreateInstancesOfCustomClassesWithGenericArgumentsOfCustomTypes() {
        Generator<?> generator = argGen.generatorFor(Polygon.class);

        Object output = generator.generate(source, status);

        assertThat(output, instanceOf(Polygon.class));
    }

}