package com.functional.java;

public interface Criterion<E>
{
    boolean test(E e);
default Criterion<E> andCriterion(Criterion<E> crit1) {
        return c -> this.test(c) && crit1.test(c);
    }
default Criterion<E> orCriterion(Criterion<E> crit1) {
        return c -> this.test(c) || crit1.test(c);
    }

default Criterion<E> negate()  {
     return e -> !this.test(e);
    }


}
