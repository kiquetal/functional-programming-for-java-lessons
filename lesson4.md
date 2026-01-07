### Working with pure functions

#### Concepts of pure functions

f(x) => x + 2
equivalent

#### Pure functions in practice

How do we make this type of functions?
We can use pure function to call impure function
Using a wrapper.


#### Planning a pipeline framework
Bucket data -> pure function -> bucket data

remember the usage of generic for the data wrapper.


#### Implementing a pipeline framework

Dealing with generic methods.

#### Coding immutable data types

Creating the map function

static <F> map(Consumer<E> consumer) 
