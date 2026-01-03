package com.functional.java;

public interface Criterion<E>
{
    public boolean test(E e);

 static <E> Criterion<E> andCriterion(Criterion<E> crit1, Criterion<E> crit2) {
        return c -> crit1.test(c) && crit2.test(c);
    }

static  <E> Criterion<E> orCriterion(Criterion<E> crit1, Criterion<E> crit2) {
        return c -> crit1.test(c) || crit2.test(c);
    }


}
