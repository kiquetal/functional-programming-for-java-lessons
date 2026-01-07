package com.functional.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SuperIterable<E> implements Iterable<E>
{

    private Iterable<E> self;
    public SuperIterable(Iterable<E> self)
    {
        this.self = self;
    }

    public SuperIterable<E> filter(Predicate<E> pred) {
        List<E> list = new ArrayList<>();

        for (E e : self)
        {
            if (pred.test(e))
            {
                list.add(e);
            }
        }

        return new SuperIterable<E>(list);
    }

    public void forEvery(Consumer<E> cons) {
        for (E e : self) {
            cons.accept(e);
        }

    }

    public <U> SuperIterable<U> map(Function<E, U> mapper) {
        List<U> results = new ArrayList<>();
        for (E e : self) {
            results.add(mapper.apply(e));
        }
        return new SuperIterable<>(results);
    }

    /**
     * Flattens a nested structure of SuperIterables into a single SuperIterable.
     * This is useful when the mapping function returns a collection (SuperIterable) for each element,
     * and we want a single flat list of all results combined.
     *
     * @param mapper A function that transforms an element E into a SuperIterable<U>
     * @param <U> The type of elements in the returned SuperIterable
     * @return A new SuperIterable containing all elements from the resulting SuperIterables
     */
    public <U> SuperIterable<U> flatMap(Function<E, SuperIterable<U>> mapper) {
        List<U> results = new ArrayList<>();
        for (E e : self) {
            SuperIterable<U> innerIterable = mapper.apply(e);
            // Iterate over the inner iterable and add each element to the flat list
            for (U u : innerIterable) {
                results.add(u);
            }
        }
        return new SuperIterable<>(results);
    }

    @Override
    public Iterator<E> iterator()
    {
        return self.iterator();
    }

    public static void main(String[] args) {
        // Create a list of integers
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        SuperIterable<Integer> superIterable = new SuperIterable<>(numbers);

        System.out.println("Testing filter method - getting even numbers:");
        SuperIterable<Integer> evenNumbers = superIterable.filter(n -> n % 2 == 0);
        evenNumbers.forEvery(n -> System.out.println("Even: " + n));

        System.out.println("\nTesting filter method - getting numbers > 5:");
        SuperIterable<Integer> largeNumbers = superIterable.filter(n -> n > 5);
        largeNumbers.forEvery(n -> System.out.println("Large: " + n));

        System.out.println("\nTesting forEvery method - printing all numbers:");
        superIterable.forEvery(n -> System.out.println("Number: " + n));

        System.out.println("\nChaining filter and forEvery:");
        superIterable
            .filter(n -> n > 3)
            .filter(n -> n < 8)
            .forEvery(n -> System.out.println("Between 3 and 8: " + n));

        System.out.println("\nTesting map method - doubling numbers:");
        superIterable
            .map(n -> n * 2)
            .forEvery(n -> System.out.println("Doubled: " + n));

        System.out.println("\nTesting map method - to String:");
        superIterable
            .map(n -> "Str-" + n)
            .forEvery(s -> System.out.println(s));

        System.out.println("\nTesting flatMap method - n -> [n, n+10]:");
        superIterable
            .filter(n -> n <= 3) // verify with a smaller set
            .flatMap(n -> new SuperIterable<>(List.of(n, n + 10)))
            .forEvery(n -> System.out.println("FlatMapped: " + n));
    }
}
