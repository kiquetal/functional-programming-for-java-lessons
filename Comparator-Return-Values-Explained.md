# Understanding Comparator Return Values

## The Question
Why does the comparator return `1` when applying the comparison?

## The Comparator Contract

A `Comparator<T>` in Java has a single method:
```java
int compare(T o1, T o2)
```

This method returns:
- **Negative integer** (typically -1): if `o1 < o2` (first object is "less than" second)
- **Zero (0)**: if `o1 == o2` (objects are equal)
- **Positive integer** (typically 1): if `o1 > o2` (first object is "greater than" second)

## Your GasComparator Implementation

```java
public class GasComparator implements Comparator<Car> {
    @Override
    public int compare(Car car1, Car car2) {
        return Double.compare(car1.getGasLevel(), car2.getGasLevel());
    }
}
```

## Your Code: Comparing with Bert's Car

```java
Car bertCar = new Car();
bertCar.setBrand("something look");
bertCar.setGasLevel(2);  // ← Bert's car has 2 gallons

GasComparator gasComparator = new GasComparator();
ToIntFunction<Car> compareWithBert = compareWithThis(gasComparator, bertCar);

// This function compares each car WITH Bert's car:
// value -> comparator.compare(value, bertCar)
//          ↓                   ↓          ↓
//       each car         has X gas    has 2 gas

cars.forEach(car -> System.out.println(compareWithBert.applyAsInt(car)));
```

## Why You Get `1` (Positive Number)

Let's trace through with your test data:

### Test Cars Gas Levels:
1. RED Toyota: **15.5** gallons
2. BLUE Honda: **8.2** gallons  
3. GREEN Ford: **20.0** gallons
4. RED BMW: **5.5** gallons
5. WHITE Toyota: **12.0** gallons

### Bert's Car: **2.0** gallons

### Comparison Results:

```
compareWithBert.applyAsInt(RED Toyota)
  = comparator.compare(RED Toyota, bertCar)
  = Double.compare(15.5, 2.0)
  = 1  ✓ (15.5 > 2.0, so return positive)

compareWithBert.applyAsInt(BLUE Honda)
  = comparator.compare(BLUE Honda, bertCar)
  = Double.compare(8.2, 2.0)
  = 1  ✓ (8.2 > 2.0, so return positive)

compareWithBert.applyAsInt(GREEN Ford)
  = comparator.compare(GREEN Ford, bertCar)
  = Double.compare(20.0, 2.0)
  = 1  ✓ (20.0 > 2.0, so return positive)

compareWithBert.applyAsInt(RED BMW)
  = comparator.compare(RED BMW, bertCar)
  = Double.compare(5.5, 2.0)
  = 1  ✓ (5.5 > 2.0, so return positive)

compareWithBert.applyAsInt(WHITE Toyota)
  = comparator.compare(WHITE Toyota, bertCar)
  = Double.compare(12.0, 2.0)
  = 1  ✓ (12.0 > 2.0, so return positive)
```

## The Answer

**You get `1` (or positive numbers) because ALL of your test cars have MORE gas than Bert's car (2.0 gallons)!**

Every car in your list has a gas level > 2.0, so when compared to Bert's car:
- `Double.compare(anyCarGas, 2.0)` returns a positive number (typically 1)

## When Would You See Different Values?

### To see `-1` (negative):
Create a car with LESS gas than Bert:
```java
Car emptyTank = new Car();
emptyTank.setGasLevel(1.0);  // Less than Bert's 2.0
compareWithBert.applyAsInt(emptyTank);  // Returns -1
```

### To see `0` (zero):
Create a car with SAME gas as Bert:
```java
Car sameTank = new Car();
sameTank.setGasLevel(2.0);  // Equal to Bert's 2.0
compareWithBert.applyAsInt(sameTank);  // Returns 0
```

### To see `1` (positive):
Any car with MORE gas than Bert:
```java
Car fullTank = new Car();
fullTank.setGasLevel(15.5);  // More than Bert's 2.0
compareWithBert.applyAsInt(fullTank);  // Returns 1
```

## Visual Summary

```
Bert's Gas: 2.0
            ↓
    --------●--------
   -1       0       +1
   ↑        ↑        ↑
  less    equal    more
  than     to      than
  Bert    Bert     Bert

Your test cars (all > 2.0):
• RED Toyota (15.5)   → returns 1
• BLUE Honda (8.2)    → returns 1
• GREEN Ford (20.0)   → returns 1
• RED BMW (5.5)       → returns 1
• WHITE Toyota (12.0) → returns 1
```

## The `compareWithThis` Helper Method

```java
private static <E> ToIntFunction<E> compareWithThis(
    Comparator<E> comparator,
    E target
) {
    return value -> comparator.compare(value, target);
    //                                  ↑       ↑
    //                               input   fixed target
}
```

This creates a **partially applied function** that:
1. Takes ONE value as input
2. Always compares it against the FIXED target (Bert's car)
3. Returns the comparison result as an int

It's a clever functional programming technique that "locks in" the second parameter of the comparator!

## Practical Use Cases

This pattern is useful for:
- Finding cars with more/less gas than a reference car
- Filtering: `cars.stream().filter(c -> compareWithBert.applyAsInt(c) > 0)`
- Sorting based on a reference point
- Creating custom predicates from comparators

