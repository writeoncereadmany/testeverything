package com.writeoncereadmany.testeverything;

import co.unruly.control.pair.Pair;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.*;
import java.util.Random;

public class ArgumentGenerator {

    private final int ATTEMPTS = 10;
    private final SourceOfRandomness source = new SourceOfRandomness(new Random());
    private final SimpleGenerationStatus status = new SimpleGenerationStatus(new GeometricDistribution(), source, 10);
    private final RecursiveGenerator recursiveGenerator;

    public ArgumentGenerator(RecursiveGenerator recursiveGenerator) {
        this.recursiveGenerator = recursiveGenerator;
    }

    public Object[] sampleArgsFor(Executable method) {
        return recursiveGenerator.generatorsForArgs(method)
            .stream()
            .map(generator -> generator.generate(source, status))
            .toArray();
    }

    public Pair<Object[], Object[]> differentArguments(Executable method) {
        Object[] firstArguments = sampleArgsFor(method);
        Object[] secondArguments = new Object[firstArguments.length];

        for(int i = 0; i < firstArguments.length; i++) {
            secondArguments[i] = exampleOfTypeDifferentTo(method.getGenericParameterTypes()[i], firstArguments[i]);
        }

        return Pair.of(firstArguments, secondArguments);
    }

    private Object exampleOfTypeDifferentTo(Type type, Object firstArgument) {
        for(int i = 0; i < ATTEMPTS; i++) {
            Object value = recursiveGenerator.generatorFor(type).generate(source, status);
            if(!value.equals(firstArgument)) {
                return value;
            }
        }
        throw new RuntimeException("Cannot find an instance of " + type.toString() + " other than " + firstArgument);
    }
}
