package com.writeoncereadmany.testeverything;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.Generators;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ArgumentGenerator {

    private final SourceOfRandomness source = new SourceOfRandomness(new Random());
    private final SimpleGenerationStatus status = new SimpleGenerationStatus(new GeometricDistribution(), source, 10);
    private final Generators repo = new GeneratorRepository(source).register(new ServiceLoaderGeneratorSource());

    public Object[] sampleArgsFor(Executable method) {
        return generatorsForArgs(method)
            .stream()
            .map(generator -> generator.generate(source, status))
            .toArray();
    }

    private List<Generator<?>> generatorsForArgs(Executable method) {
        return Stream.of(method.getParameterTypes())
            .map(param -> repo.type(param))
            .collect(toList());
    }
}
