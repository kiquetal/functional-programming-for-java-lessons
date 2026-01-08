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

**Visualizing Map vs FlatMap:**

1.  **Map**: Transforms each element individually. Structure is preserved (1-to-1).
    ```text
    Input:  [ A,       B,       C       ]
              |        |        |
             map(x -> x.toLowerCase())
              |        |        |
    Output: [ a,       b,       c       ]
    ```

2.  **FlatMap**: Transforms each element into a *collection*, then flattens them (1-to-Many flattened).
    ```text
    Input:  [ A,       B                ]
              |        |
             map(x -> [x, x.toLowerCase()])  <-- Step 1: Transformation produces lists
              |        |
    Interm: [ [A, a],  [B, b]           ]
              |        |
             flatten()                       <-- Step 2: Flattens the nested lists
              |
    Output: [ A, a, B, b                ]
    ```


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

**Why does this work? (Visualizing Scope)**

The key is that the inner `map` runs *inside* the curly braces of the `flatMap`. Because of this, it can "see" the `user` variable (this is called a closure).

```text
Stream: [ User(Alice) ] -------------------------------------> ...
             |
             v
   flatMap( user -> {  // <--- 'user' is User(Alice) here
             |
             |   // We get Alice's emails: [e1, e2]
             |   new SuperIterable(user.getEmails())
             |
             |   // INTERNAL MAP:
             |   // Runs on [e1, e2].
             |   // crucially: IT CAN READ 'user' from above!
             |        |
             |       map( email -> user.getName() + " - " + email )
             |        |             ^ captured from outer scope
             |        v
             |   Returns: [ "Alice - e1", "Alice - e2" ]
             |
   } ) // End of flatMap lambda
             |
             v
   // flatMap takes the list [ "Alice - e1", "Alice - e2" ] and "peels" the list wrapper.
   Result: "Alice - e1", "Alice - e2", ...
```

### The "Box Opener" Analogy (Why Map inside FlatMap?)

Think of `flatMap` as a **Box Opener**. It takes a Box (a List) from you, opens it, and dumps the contents onto a conveyor belt.

**Scenario A: Without Inner Map**
1. You have `User(Alice)`.
2. You grab her box of emails: `[e1, e2]`.
3. You hand the box to `flatMap`.
4. `flatMap` dumps them: `e1`, `e2`.
   *Problem:* Once they are on the belt, we forgot they came from Alice!

**Scenario B: With Inner Map (The Solution)**
1. You have `User(Alice)`.
2. You grab her box of emails: `[e1, e2]`.
3. **Crucial Step (Inner Map):** Before handing the box over, you use `map` to write "Alice" on every item *inside* the box.
   - The box now contains: `["Alice-e1", "Alice-e2"]`.
4. *Now* you hand the box to `flatMap`.
5. `flatMap` dumps them: `"Alice-e1"`, `"Alice-e2"`.
   *Result:* The context is preserved!

#### Another wrapper

Optional?
