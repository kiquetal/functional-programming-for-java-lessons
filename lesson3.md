#### Lesson3: The power of combinations and modifications


#### Improving the behaviour factories

Maybe using the Function<F> 

use the context


#### Requirements for closure

stack-frame: location where the method is alocated.

getGass..(threshold) {

 return c-> g.getGass() >= threshold

}

Changing the value of a variable is not allowed.
Immutability fits better.

Remember about closure, to obtain a reference before returning a function or lambda

#### Combining behaviors - part 1

filter
map
combineThen

Let's create the behaviour

#### Combiging behaviors - part 2 

We create an interface with static and implementation


#### Interfaces for lambdas

F f(E e) -> Function<E,F>
boolean f(E e) -> Predicate<E>
void f(E e) -> Consumer<E>
E f() -> Supplier<E>
E f(E e) -> unary binary Operator<E>

To {Int,Long,Double} -> return type


#### Using Predicates in the example

We just receive an argument and return boolean
