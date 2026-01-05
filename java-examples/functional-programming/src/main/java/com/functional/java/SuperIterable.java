package com.functional.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
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
    }
}
