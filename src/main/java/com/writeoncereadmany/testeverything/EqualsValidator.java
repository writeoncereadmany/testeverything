package com.writeoncereadmany.testeverything;

import co.unruly.control.pair.Pair;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.writeoncereadmany.testeverything.ClassPredicates.implementing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class EqualsValidator {

    private final ArgumentGenerator generator = new ArgumentGenerator(new RecursiveGenerator());

    public void assertEqualsMethodsSound(String inPackage) throws Exception {
        assertThat(violationsOfEqualsContract(inPackage), empty());
    }

    private List<String> violationsOfEqualsContract(String basePackage) throws Exception {
        List<Class<?>> typesImplementingEquals = ClassFinder.findClasses(
            EqualsValidator.class.getClassLoader(),
            basePackage,
            implementing("equals", Object.class));

        return typesImplementingEquals
            .stream()
            .flatMap(this::problemsWithEquals)
            .collect(toList());
    }

    private Stream<String> problemsWithEquals(Class<?> type) {
        try {
            Constructor<?> constructor = Stream.of(type.getConstructors())
                .sorted(comparingInt(Constructor::getParameterCount))
                .findFirst()
                .orElseThrow(RuntimeException::new);

            Pair<Object[], Object[]> arguments = generator.differentArguments(constructor);

            return Stream.of(
                equalsWithSameArgs(constructor, arguments),
                equalsWithDifferentArgs(constructor, arguments)
            ).flatMap(i -> i);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Stream<String> equalsWithSameArgs(Constructor<?> constructor, Pair<Object[], Object[]> args) {
        try {
            Object first = constructor.newInstance(args.left);
            Object second = constructor.newInstance(args.left);
            if (first.equals(second)) {
                return Stream.empty();
            } else {
                return Stream.of(
                    first.getClass() +
                        " does not equal itself when constructed with same parameters of " +
                        Arrays.toString(args.left));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<String> equalsWithDifferentArgs(Constructor<?> constructor, Pair<Object[], Object[]> args) {
        try {
            List<String> errors = new ArrayList<>();

            for (int i = 0; i < args.left.length; i++) {
                Object[] differentArgs = Arrays.copyOf(args.left, args.left.length);
                differentArgs[i] = args.right[i];


                Object first = constructor.newInstance(args.left);
                Object second = constructor.newInstance(differentArgs);

                if(first.equals(second)) {
                    errors.add(
                        first.getClass() +
                        " is equal to instances of itself where the parameter in position " + i +
                        " is different: specifically " + args.left[i] + " and " + differentArgs[i]
                    );
                }
            }
            return errors.stream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
