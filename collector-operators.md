# Collector Operators & Comparison Utilities

To understand the core of `Collectors`, think of them as the "finish line" of a Stream. They transform the stream of elements into a final result.

## 6 Key Utility Methods

These methods are essential for sorting and comparing elements before collection, or defining order within specific collectors.

1.  **`Map.Entry.comparingByKey()`**
    *   Returns a `Comparator` that compares `Map.Entry` objects by their key.
2.  **`Map.Entry.comparingByValue()`**
    *   Returns a `Comparator` that compares `Map.Entry` objects by their value.
3.  **`Comparator.comparing(keyExtractor)`**
    *   Accepts a function (the **key extractor**) and returns a `Comparator` that compares by that extracted key.
4.  **`Comparator.naturalOrder()`**
    *   Returns a `Comparator` that compares `Comparable` objects in natural order.
5.  **`Comparator.reverseOrder()`**
    *   Returns a `Comparator` that imposes the reverse of the natural ordering.
6.  **`Comparator.nullsFirst(Comparator other)`**
    *   Returns a null-friendly comparator that considers `null` to be less than non-null values.

---

## Core Concept: The Key Extractor

A **Key Extractor** is simply a function (usually a lambda or method reference) that extracts a value from an object to be used for comparison or grouping.

*   In `Comparator.comparing(User::getName)`, `User::getName` is the key extractor. The comparator uses the *names* to decide the order of the *Users*.
*   In `Collectors.groupingBy(User::getCity)`, `User::getCity` is the key extractor (often called the "classifier" in this context).

---

## Collectors.groupingBy

`groupingBy` is one of the most powerful collectors. It partitions the stream elements into groups based on a classification function (the key extractor).

It generally returns a `Map<K, List<V>>`, where `K` is the classification key and the value is a List of items that match that key.

### Common Overloads
1.  **`groupingBy(classifier)`**: Groups elements into a `List`.
2.  **`groupingBy(classifier, downstream)`**: Groups elements, but instead of a List, applies another collector (downstream) to the values.

---

## Examples

Assume the following `User` class:

```java
class User {
    private String name;
    private String city;
    private int age;

    public User(String name, String city, int age) {
        this.name = name;
        this.city = city;
        this.age = age;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public int getAge() { return age; }
    
    @Override
    public String toString() { return name; }
}
```

### 1. Map of User: Sorting with `comparingByValue`
Scenario: We have a `Map<Integer, User>` (ID -> User) and want to sort it by User Name.

```java
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;

public class SortMapExample {
    public static void main(String[] args) {
        Map<Integer, User> userMap = new HashMap<>();
        userMap.put(1, new User("Alice", "London", 30));
        userMap.put(2, new User("Charlie", "New York", 25));
        userMap.put(3, new User("Bob", "London", 35));

        Map<Integer, User> sortedByName = userMap.entrySet().stream()
            // 1. comparingByValue uses the User object's natural order or a provided comparator
            // 2. Comparator.comparing extracts the name to sort by
            .sorted(Entry.comparingByValue(Comparator.comparing(User::getName)))
            .collect(Collectors.toMap(
                Entry::getKey,
                Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new // Use LinkedHashMap to preserve the sorted order
            ));

        System.out.println(sortedByName);
    }
}
```

### 2. Grouping Users by City (`groupingBy`)
Scenario: Group a list of users by their city.

```java
List<User> users = Arrays.asList(
    new User("Alice", "London", 30),
    new User("Charlie", "New York", 25),
    new User("Bob", "London", 35)
);

// Simple Grouping
// Result: { "London"=[Alice, Bob], "New York"=[Charlie] }
Map<String, List<User>> usersByCity = users.stream()
    .collect(Collectors.groupingBy(User::getCity));

// Grouping with Downstream Collector (Counting)
// Result: { "London"=2, "New York"=1 }
Map<String, Long> countByCity = users.stream()
    .collect(Collectors.groupingBy(User::getCity, Collectors.counting()));
```
