# `collect` vs `reduce` in Java Streams

In Java's functional programming model with Streams, both `collect` and `reduce` are **terminal operations** used to combine the elements of a stream into a single result. However, they serve different purposes and have distinct performance characteristics.

## Key Differences

| Feature | `reduce` | `collect` |
| :--- | :--- | :--- |
| **Type** | General purpose reduction (immutable). | Mutable reduction. |
| **Mechanism** | Repeatedly applies a binary operator to combine two elements into a new result. | Mutates an existing container (like a `List`, `Map`, or `StringBuilder`) to accumulate elements. |
| **Intended Use** | Calculating a single value (sum, min, max) or immutable data transformations. | Accumulating elements into collections or complex mutable objects. |
| **Parallelism** | Requires an associative function. | Requires a concurrent collector or synchronization logic (handled by standard collectors). |

---

## Efficiency & Performance

**`collect` is strictly preferred for mutable containers.**

If you attempt to use `reduce` to build a `List` or `String`, you incur a massive performance penalty. Since `reduce` expects immutability, you would have to create a *new* list copy every time you add an element to keep the operation pure. This turns a linear $O(N)$ operation into a quadratic $O(N^2)$ operation.

`collect` avoids this by modifying the container in place.

---

## Real World Use Cases

### Case 1: Calculating a Total (Use `reduce`)
**Scenario:** You have a list of invoices and want to calculate the total revenue.
Since the result is a single immutable scalar (Double/BigDecimal), `reduce` is perfect.

```java
List<Double> invoiceAmounts = Arrays.asList(100.50, 200.00, 50.25);

// Efficient and idiomatic
Double total = invoiceAmounts.stream()
    .reduce(0.0, (subtotal, element) -> subtotal + element);
```

### Case 2: Grouping or Listing (Use `collect`)
**Scenario:** You want to filter a list of users and put the names of the active ones into a new `List`.
Using `collect` allows you to add items to an `ArrayList` efficiently.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// PREFERRED: Collect
// Efficiently adds to a mutable ArrayList
List<String> upperCaseNames = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

## The "Don't Do This" Example

Here is a demonstration of why `reduce` is bad for accumulating lists.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReduceVsCollect {

    public static void main(String[] args) {
        List<String> items = Arrays.asList("a", "b", "c", "d", "e");

        // ❌ BAD: Using reduce to return a List
        // This is inefficient because a NEW List is created and copied for every element.
        List<String> reducedList = items.stream()
            .reduce(new ArrayList<>(), (acc, item) -> {
                List<String> newAcc = new ArrayList<>(acc); // Copy overhead!
                newAcc.add(item);
                return newAcc;
            }, (acc1, acc2) -> {
                List<String> newAcc = new ArrayList<>(acc1);
                newAcc.addAll(acc2);
                return newAcc;
            });

        // ✅ GOOD: Using collect
        // Modifies the ArrayList in place. No copying overhead.
        List<String> collectedList = items.stream()
            .collect(Collectors.toList());
    }
}
```

### Summary
*   Use **`reduce`** when "folding" a stream into a single immutable value (sum, product, max).
*   Use **`collect`** when accumulating elements into a container (List, Map, Set, StringBuilder).
