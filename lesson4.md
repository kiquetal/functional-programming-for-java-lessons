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

### Map vs FlatMap: The Critical Difference

#### When to use what?

*   **Use `map` when:**
    *   One input element produces exactly one output element (1-to-1).
    *   The transformation returns a simple object (e.g., `String`, `Integer`, `UserDto`).
    *   You want to transform data *without* changing the structure of the stream.

*   **Use `flatMap` when:**
    *   One input element produces **multiple** output elements (1-to-Many).
    *   One input element produces a **Collection** or a **Stream** (e.g., `List<String>`, `Stream<User>`).
    *   You are dealing with nested structures (e.g., `List<List<Data>>`) and want a single flattened list.
    *   You are chaining operations that return "wrapper" types like `Optional`.

#### Real-World Examples using Java Streams

##### 1. The E-Commerce "Order Items" (1-to-Many)
**Scenario:** You have a list of `Order` objects. Each `Order` has a list of `LineItem`s. You want a list of *all* line items sold today to calculate totals.

*   **The "Moment" `flatMap` is needed:** When you ask for `order.getLineItems()`, you get a `List<LineItem>`. If you use `map`, you end up with a Stream of Lists (`Stream<List<LineItem>>`). The "moment" is realizing you don't want a Stream of Lists, you just want the Items.

*   **Using `map` (Incorrect for this goal):**
    ```java
    List<Order> orders = ...;
    // Result is List<List<LineItem>> - Nested and hard to process!
    List<List<LineItem>> nestedItems = orders.stream()
        .map(order -> order.getLineItems())
        .collect(Collectors.toList());
    ```

*   **Using `flatMap` (Correct):**
    ```java
    // Result is List<LineItem> - Flat and ready for processing!
    List<LineItem> allItems = orders.stream()
        .flatMap(order -> order.getLineItems().stream()) // Converts List<LineItem> to Stream<LineItem>
        .collect(Collectors.toList());
    ```

##### 2. The Social Media "Hashtag Search" (Stream flattening)
**Scenario:** You have a list of `User`s. Each `User` has a method `getPosts()` returning a `List<Post>`. You want to find all posts containing "#java".

```java
List<User> users = ...;

List<Post> javaPosts = users.stream()
    // 1. Transform User -> Stream<Post>
    // 2. Flatten Stream<Stream<Post>> -> Stream<Post>
    .flatMap(user -> user.getPosts().stream()) 
    .filter(post -> post.getContent().contains("#java"))
    .collect(Collectors.toList());
```

##### 3. The "Optional" Chaining (Avoiding `Optional<Optional<T>>`)
**Scenario:** You have a `Person`. A Person *might* have a `Car`. A Car *might* have `Insurance`. You want the insurance name safely.

```java
Optional<Person> personOpt = ...;

// map() would wrap the result again, leading to nested Optionals
// Optional<Optional<Car>> nested = personOpt.map(Person::getCar); 

// flatMap() "peels" the wrapper preventing nesting
String insuranceName = personOpt
    .flatMap(Person::getCar)           // Returns Optional<Car>, not Optional<Optional<Car>>
    .flatMap(Car::getInsurance)        // Returns Optional<Insurance>
    .map(Insurance::getName)           // getName returns String (simple 1-to-1), so we use map
    .orElse("Unknown");
```

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

##### 4. The "File System Search" (Reading lines from multiple files)
**Scenario:** You have a list of file paths. You want to find all lines across all files that contain the word "ERROR".

*   **Using `map`:** You would get a `Stream<List<String>>` (a stream where each element is a list of lines from one file).
*   **Using `flatMap`:** You get a `Stream<String>` (a single stream of all lines from all files).

```java
List<Path> paths = Arrays.asList(Paths.get("log1.txt"), Paths.get("log2.txt"));

List<String> errors = paths.stream()
    .flatMap(path -> {
        try {
            return Files.lines(path); // Returns Stream<String>
        } catch (IOException e) {
            return Stream.empty();    // Gracefully handle errors by returning empty stream
        }
    })
    .filter(line -> line.contains("ERROR"))
    .collect(Collectors.toList());
```

#### The `Optional` Wrapper: Avoiding the "Nested Box"

Just like Streams, `Optional` has both `map` and `flatMap`. The logic is identical: `flatMap` prevents you from wrapping a value in multiple layers of `Optional`.

| Method | Function Signature | Result if Function returns `Optional<U>` |
| :--- | :--- | :--- |
| **`map`** | `Optional<T>.map(T -> U)` | `Optional<Optional<U>>` (Nested) |
| **`flatMap`** | `Optional<T>.flatMap(T -> Optional<U>)` | `Optional<U>` (Flattened) |

**Real-World Example: User Settings**
Imagine a system where a `User` might have `Settings`, and `Settings` might have a `Theme`.

```java
public class User {
    public Optional<Settings> getSettings() { ... }
}

public class Settings {
    public Optional<Theme> getTheme() { ... }
}

// THE WRONG WAY (using map)
Optional<Optional<Theme>> nestedTheme = user.getSettings()
    .map(Settings::getTheme); // Result is Optional<Optional<Theme>> - Useless!

// THE RIGHT WAY (using flatMap)
Optional<Theme> theme = user.getSettings()
    .flatMap(Settings::getTheme); // Result is Optional<Theme> - Clean!
```

### Handling Lists with Optional Fields: The `map` + `orElse` Pattern

A common requirement is transforming a List of objects where each object has an `Optional` field, and you want to extract that field providing a default value if it's missing.

**Scenario:** You have a list of `User`s. Each `User` has a method `getNickname()` which returns `Optional<String>`. You want a list of all nicknames, using "Anonymous" if the nickname is missing.

**The Solution:** Use `Stream.map` combined with `Optional.orElse`.

```java
List<User> users = ...;

List<String> nicknames = users.stream()
    // 1. user.getNickname() returns Optional<String>
    // 2. .orElse("Anonymous") unwraps it, returning the string "Anonymous" if empty.
    .map(user -> user.getNickname().orElse("Anonymous"))
    .collect(Collectors.toList());
```

**Why not `flatMap` here?**
If you used `flatMap` (e.g., converting the Optional to a Stream), you would filter out the users without nicknames entirely (because an empty Optional becomes an empty Stream).

*   **Use `map` + `orElse`** when you want to **keep the same number of elements**, replacing missing data with defaults.
*   **Use `flatMap`** (turning Optional to Stream) when you want to **filter out** missing data.

### Summary: The "Type" Rule of Thumb

If you are inside a pipeline of type `Container<T>` (where Container is `Stream`, `Optional`, `CompletableFuture`, etc.):

1.  If your transformation returns a **simple value** (`String`, `int`): Use **`map`**.
2.  If your transformation returns **another Container** (`Stream<U>`, `Optional<U>`): Use **`flatMap`**.
