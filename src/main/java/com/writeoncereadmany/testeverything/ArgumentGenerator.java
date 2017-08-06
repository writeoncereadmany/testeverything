package com.writeoncereadmany.testeverything;

import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.generator.GeneratorRepository;
import com.pholser.junit.quickcheck.internal.generator.ServiceLoaderGeneratorSource;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.writeoncereadmany.testeverything.examples.Point;

import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static co.unruly.control.HigherOrderFunctions.peek;
import static co.unruly.control.result.Introducers.ifType;
import static co.unruly.control.result.Match.matchValue;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class ArgumentGenerator {

    private final SourceOfRandomness source = new SourceOfRandomness(new Random());
    private final SimpleGenerationStatus status = new SimpleGenerationStatus(new GeometricDistribution(), source, 10);
    private final GeneratorRepository repo = new GeneratorRepository(source).register(new ServiceLoaderGeneratorSource());

    public Object[] sampleArgsFor(Executable method) {
        repo.register(repo.constructor(Point.class, int.class, int.class));

        return generatorsForArgs(method)
            .stream()
            .map(generator -> generator.generate(source, status))
            .toArray();
    }

    public Generator<?> generatorFor(Type param) {
        return matchValue(param,
            ifType(Class.class, this::generatorForSimpleType),
            ifType(ParameterizedType.class, this::generatorForParameterizedType)
        ).otherwise(p -> {throw new RuntimeException("Cannot deal with params of type " + p.getClass()); } );
    }

    private List<Generator> generatorsForArgs(Executable method) {
        return Stream.of(method.getGenericParameterTypes())
            .map(this::generatorFor)
            .collect(toList());
    }

    private Generator generatorForParameterizedType(ParameterizedType param) {
        List<Generator<?>> componentGenerators = Stream.of(param.getActualTypeArguments())
            .map(this::generatorFor)
            .peek(repo::register)
            .collect(toList());
        Generator<?> generator = repo.generatorFor(new ParameterTypeContext((Class) param.getRawType()));
        generator.addComponentGenerators(componentGenerators);
        return generator;
    }

    private Generator<?> generatorForSimpleType(Class param) {
        try {
            return repo.type(param);
        } catch (Exception ex) {
            Optional<Generator<?>> generator1 = Stream.of(param.getConstructors())
                .sorted(comparingInt(Constructor::getParameterCount))
                .filter(constructor -> constructor.getParameterCount() > 0)
                .findFirst()
                .map(peek(c -> buildGeneratorsFor(c.getGenericParameterTypes())))
                .map(c -> repo.constructor(param, c.getParameterTypes()));
            Generator<?> generator = generator1
                .orElseGet(() -> repo.fieldsOf(param));
            return generator;
        }
    }

    private void buildGeneratorsFor(Type... types) {
        Stream.of(types).map(this::generatorFor).forEach(repo::register);
    }

}
