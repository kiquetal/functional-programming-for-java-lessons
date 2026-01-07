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

#### Remember the difference between map and flatmap


Always think if you return a List, Stream, and you want a single colleciton use flatmap


#### Handling Lost Boundaries in FlatMap

When using `flatMap`, you flatten a nested structure (like `User -> List<Email>`) into a single sequence (just `Email`s). A common issue is "losing the boundary" or the context of the outer element (the `User`).

To resolve this using `SuperIterable` as is:
Embed a `map` operation *inside* the function passed to `flatMap`. This allows you to capture the outer element (context) and pair it with the inner element.

Example (`com.functional.java.PreservingContextExample`):
```java
users.flatMap(user -> 
    new SuperIterable<>(user.getEmails())
        .map(email -> "User: " + user.getName() + " -> " + email)
)
```

This preserves the `user` context for each `email` in the resulting flat list.

#### Another wrapper

Optional?
