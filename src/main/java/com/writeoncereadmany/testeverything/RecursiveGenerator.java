package com.writeoncereadmany.testeverything;

import co.unruly.control.result.Resolvers;
import co.unruly.control.result.Result;
import com.google.common.reflect.ClassPath;
import com.pholser.junit.quickcheck.generator.Ctor;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.ParameterTypeContext;
import com.pholser.junit.quickcheck.internal.Weighted;
import com.pholser.junit.quickcheck.internal.generator.*;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import static co.unruly.control.HigherOrderFunctions.peek;
import static co.unruly.control.Predicates.not;
import static co.unruly.control.result.Introducers.ifType;
import static co.unruly.control.result.Match.matchValue;
import static co.unruly.control.result.Result.failure;
import static co.unruly.control.result.Result.success;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class RecursiveGenerator {

    private final SourceOfRandomness source = new SourceOfRandomness(new Random());
    private final SimpleGenerationStatus status = new SimpleGenerationStatus(new GeometricDistribution(), source, 10);
    private final GeneratorRepository repo = new GeneratorRepository(source).register(new ServiceLoaderGeneratorSource());

    public Generator<?> generatorFor(Type param) {
        return matchValue(param,
            ifType(Class.class, this::generatorForSimpleType),
            ifType(ParameterizedType.class, this::generatorForParameterizedType),
            ifType(WildcardType.class, this::generatorForWildcardType)
        ).otherwise(p -> {throw new RuntimeException("Cannot deal with params of type " + p.getClass() + " named " + p); } );
    }

    public List<Generator> generatorsForArgs(Executable method) {
        return Stream.of(method.getGenericParameterTypes())
            .map(this::generatorFor)
            .collect(toList());
    }

    private Generator generatorForWildcardType(WildcardType wildcard) {
        List<Weighted<Generator<?>>> upperBoundsTypes = Stream
            .of(wildcard.getUpperBounds())
            .map(this::generatorFor)
            .<Weighted<Generator<?>>>map(g -> new Weighted<>(g, 1))
            .collect(toList());
        CompositeGenerator compositeGenerator = new CompositeGenerator(upperBoundsTypes);
        repo.register(compositeGenerator);
        return compositeGenerator;
    }

    private Generator generatorForParameterizedType(ParameterizedType param) {
        Stream.of(param.getActualTypeArguments())
            .map(this::generatorFor)
            .forEach(repo::register);
        return repo.generatorFor(new ParameterTypeContext((Class) param.getRawType()));
    }

    private Generator<?> generatorForSimpleType(Class param) {
        try {
            return repo.type(param);
        } catch (Exception ex) {
            // need to check for array first, as apparently arrays are abstract!
            if(param.isArray()) {
                return generatorForArray(param);
            }

            if(param.isInterface() || Modifier.isAbstract(param.getModifiers())) {
                return generatorForSubtypesOf(param);
            }

            Optional<Generator<?>> generator1 = Stream.of(param.getDeclaredConstructors())
                .sorted(comparingInt(Constructor::getParameterCount))
                .filter(constructor -> constructor.getParameterCount() > 0)
                .findFirst()
                .map(peek(c -> buildGeneratorsFor(c.getGenericParameterTypes())))
                .map(this::buildConstructor);
            Generator<?> generator = generator1
                .orElseGet(() ->
                    repo.fieldsOf(param));
            repo.register(generator);
            return generator;
        }
    }

    private Generator<?> buildConstructor(Constructor<?> cons) {
        cons.setAccessible(true);
        Ctor<?> ctor = new Ctor<>(cons);
        ctor.provide(repo);
        ctor.configure(cons);
        return ctor;
    }

    private Generator<?> generatorForSubtypesOf(Class<?> iface) {
        try {
            List<Weighted<Generator<?>>> implementationGenerators = ClassPath
                .from(getClass().getClassLoader())
                .getAllClasses()
                .stream()
                .map(tryTo(ClassPath.ClassInfo::load))
                .flatMap(Resolvers.successes())
                .filter(iface::isAssignableFrom)
                .filter(not(Class::isInterface))
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .map(this::generatorFor)
                // totally unsure why this is the point where I need a type hint, but hey
                .<Weighted<Generator<?>>>map(generator -> new Weighted<>(generator, 1))
                .collect(toList());

            if(implementationGenerators.isEmpty()) {
                throw new RuntimeException("No types exist that implement " + iface);
            }

            CompositeGenerator compositeGenerator = new CompositeGenerator(implementationGenerators);
            repo.register(compositeGenerator);
            return compositeGenerator;

        } catch (Exception ex) {
            throw new RuntimeException(ex + " occurred while generating for type " + iface);
        }
    }

    private Generator<?> generatorForArray(Class<?> array) {
        Generator<?> componentGenerator = generatorFor(array.getComponentType());
        repo.register(componentGenerator);
        ArrayGenerator arrayGenerator = new ArrayGenerator(array.getComponentType(), componentGenerator);
        repo.register(arrayGenerator);
        return arrayGenerator;
    }

    private void buildGeneratorsFor(Type... types) {
        Stream.of(types).map(this::generatorFor).forEach(repo::register);
    }

    private static <I, O, T extends Throwable> Function<I, Result<O, Throwable>> tryTo(TantrumFunction<I, O, T> f) {
        return i -> {
            try {
                return success(f.apply(i));
            } catch (Throwable t) {
                return failure(t);
            }
        };
    }

    private interface TantrumFunction<I, O, T extends Throwable> {
        O apply(I input) throws T;
    }
}
