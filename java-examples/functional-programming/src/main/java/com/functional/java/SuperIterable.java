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

    private SuperIterable<E> filter(Predicate<E> pred) {
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

    private void forEvery(Consumer<E> cons) {
        for (E e : self) {
            cons.accept(e);
        }

    }
    @Override
    public Iterator<E> iterator()
    {
        return self.iterator();
    }
}
