# Java Method References

Method references are a shorthand notation for lambdas that call a specific method. They make your code more readable by removing boilerplate code when a lambda simply forwards its arguments to a method.

There are four main types of method references:

---

## 1. Reference to a Static Method
**Syntax:** `ClassName::staticMethodName`

Instead of: `(args) -> ClassName.staticMethod(args)`
Use: `ClassName::staticMethod`

```java
// Example: Converting a list of strings to integers
List<String> numbers = Arrays.asList("1", "2", "3");
List<Integer> ints = numbers.stream()
    .map(Integer::parseInt) // Static method reference
    .collect(Collectors.toList());
```

---

## 2. Reference to an Instance Method of a Particular Object
**Syntax:** `containingObject::instanceMethodName`

Instead of: `(args) -> containingObject.instanceMethod(args)`
Use: `containingObject::instanceMethod`

```java
// Example: Printing each element using System.out
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.forEach(System.out::println); // Reference to instance method of 'out' object
```

---

## 3. Reference to an Instance Method of an Arbitrary Object of a Particular Type
**Syntax:** `ClassName::instanceMethodName`

Instead of: `(arg1, rest) -> arg1.instanceMethod(rest)`
Use: `ClassName::instanceMethod`

The first argument of the lambda becomes the target of the method call.

```java
// Example: Sorting strings ignoring case
List<String> cities = Arrays.asList("London", "new york", "Paris");
cities.sort(String::compareToIgnoreCase); // 'this' is the first string compared
```

```java
// Example: Mapping User objects to their names
List<User> users = ...;
List<String> names = users.stream()
    .map(User::getName) // Reference to instance method of User class
    .collect(Collectors.toList());
```

---

## 4. Reference to a Constructor
**Syntax:** `ClassName::new`

Instead of: `(args) -> new ClassName(args)`
Use: `ClassName::new`

```java
// Example: Creating a new ArrayList from a stream
List<String> list = stream.collect(Collectors.toCollection(ArrayList::new));
```

```java
// Example: Mapping names to User objects
List<String> names = Arrays.asList("Alice", "Bob");
List<User> users = names.stream()
    .map(User::new) // Calls 'new User(name)'
    .collect(Collectors.toList());
```

---

## Summary Table

| Type | Syntax | Lambda Equivalent |
| :--- | :--- | :--- |
| **Static Method** | `ClassName::staticMethod` | `(args) -> ClassName.staticMethod(args)` |
| **Instance Method (Object)** | `obj::instanceMethod` | `(args) -> obj.instanceMethod(args)` |
| **Instance Method (Type)** | `ClassName::instanceMethod` | `(obj, args) -> obj.instanceMethod(args)` |
| **Constructor** | `ClassName::new` | `(args) -> new ClassName(args)` |
