package com.writeoncereadmany.testeverything;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.writeoncereadmany.testeverything.examples.*;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class RecursiveGeneratorTest {

    private final SourceOfRandomness source = new SourceOfRandomness(new Random());
    private final SimpleGenerationStatus status = new SimpleGenerationStatus(new GeometricDistribution(), source, 10);
    private final RecursiveGenerator argGen = new RecursiveGenerator();

    @Test
    public void canCreateInstancesOfCustomClasses() {
        assertCanGenerateInstanceOf(Point.class);
    }

    @Test
    public void canCreateInstancesOfCustomClassesWithCustomArguments() {
        assertCanGenerateInstanceOf(Circle.class);
    }

    @Test
    public void canCreateInstancesOfInterfaces() {
        assertCanGenerateInstanceOf(Shape.class);
    }

    @Test
    public void canCreateInstancesOfCustomClassesWithGenericArgumentsOfCustomTypes() {
        assertCanGenerateInstanceOf(Polygon.class);
    }

    @Test
    public void canCreateInstancesOfCustomClassesWithFunctionalInterfacesOverCustomTypes() {
        assertCanGenerateInstanceOf(ColorFilter.class);
    }

    @Test
    public void canCreateInstancesOfCustomClassesWithVarargsOfCustomTypes() {
        assertCanGenerateInstanceOf(Picture.class);
    }

    @Test
    public void canCreateInstancesOfEnums() {
        assertCanGenerateInstanceOf(Color.class);
    }

    @Test
    public void canCreateInstancesOfCustomTypesWithPrivateConstructors() {
        assertCanGenerateInstanceOf(Rectangle.class);
    }

    @Test
    public void canCreateInstancesOfAbstractTypes() {
        assertCanGenerateInstanceOf(Converging.class);
    }

    private void assertCanGenerateInstanceOf(Class<?> param) {
        Generator<?> generator = argGen.generatorFor(param);

        Object output = generator.generate(source, status);

        assertThat(output, instanceOf(param));
    }

}
