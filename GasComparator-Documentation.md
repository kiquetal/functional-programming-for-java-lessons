# GasComparator for Car Class

## Overview
The `GasComparator` is a custom comparator that allows you to compare and sort `Car` objects based on their gas level.

## Implementation

```java
package com.functional.java;

import java.util.Comparator;

/**
 * Comparator for comparing Cars based on their gas level.
 * Cars with higher gas levels are considered "greater".
 */
public class GasComparator implements Comparator<Car> {

    @Override
    public int compare(Car car1, Car car2) {
        // Compare gas levels using Double.compare
        return Double.compare(car1.getGasLevel(), car2.getGasLevel());
    }
}
```

## Key Features

1. **Implements Comparator<Car>**: Standard Java interface for comparison
2. **Uses Double.compare()**: Properly handles double precision comparison
3. **Ascending order by default**: Lower gas levels come first
4. **Reversible**: Can be easily reversed for descending order

## Car Class Updates

The `Car` class now includes a `gasLevel` property:

```java
class Car {
    private String color;
    private String brand;
    private double gasLevel; // Gas level in gallons or liters

    // Getters and Setters
    public double getGasLevel() { return gasLevel; }
    public void setGasLevel(double gasLevel) { this.gasLevel = gasLevel; }
    
    // ... other methods
}
```

## Usage Examples

### 1. Sort Cars by Gas Level (Ascending)
```java
List<Car> cars = createTestCars();
cars.sort(new GasComparator());
// Result: Cars ordered from lowest to highest gas level
```

### 2. Sort Cars by Gas Level (Descending)
```java
List<Car> cars = createTestCars();
cars.sort(new GasComparator().reversed());
// Result: Cars ordered from highest to lowest gas level
```

### 3. Find Car with Minimum Gas
```java
Car minGasCar = cars.stream()
    .min(new GasComparator())
    .orElse(null);
System.out.println("Car with lowest gas: " + minGasCar);
```

### 4. Find Car with Maximum Gas
```java
Car maxGasCar = cars.stream()
    .max(new GasComparator())
    .orElse(null);
System.out.println("Car with most gas: " + maxGasCar);
```

### 5. Combine with Criterion for Filtering
```java
// Filter cars with gas level > 10.0
showAll(cars, c -> c.getGasLevel() > 10.0);

// Combine with color criterion: RED cars with high gas
Criterion<Car> redWithHighGas = getColorCriterion("RED")
    .andCriterion(c -> c.getGasLevel() > 10.0);
showAll(cars, redWithHighGas);
```

### 6. Lambda Alternative (Without Creating GasComparator class)
```java
// Using lambda expression for one-time use
cars.sort((car1, car2) -> Double.compare(car1.getGasLevel(), car2.getGasLevel()));

// Or using Comparator.comparingDouble
cars.sort(Comparator.comparingDouble(Car::getGasLevel));
```

## When to Use GasComparator vs Lambda

### Use GasComparator class when:
- You need to reuse the comparison logic in multiple places
- You want clear, self-documenting code
- You're building a library or API
- You need a named type for better error messages

### Use Lambda/Method Reference when:
- One-time, inline usage
- Simple, straightforward comparisons
- Quick prototyping

## Test Examples

The test suite includes:
- ✓ Ascending sort verification
- ✓ Descending sort verification
- ✓ Finding min/max gas levels
- ✓ Equal value comparison
- ✓ Integration with Criterion interface
- ✓ Combined filtering and sorting

## Functional Programming Concepts Demonstrated

1. **Comparator Interface**: Functional interface with single abstract method `compare()`
2. **Strategy Pattern**: Encapsulates comparison algorithm
3. **Composition**: Can be combined with `reversed()`, `thenComparing()`, etc.
4. **Higher-Order Functions**: Comparator can be passed as parameter to `sort()`, `min()`, `max()`
5. **Immutability**: Comparison doesn't modify the Car objects

## Alternative Implementations

### Using Comparator.comparingDouble (Java 8+)
```java
Comparator<Car> gasComparator = Comparator.comparingDouble(Car::getGasLevel);
```

### With Null Safety
```java
public class GasComparator implements Comparator<Car> {
    @Override
    public int compare(Car car1, Car car2) {
        if (car1 == null && car2 == null) return 0;
        if (car1 == null) return -1;
        if (car2 == null) return 1;
        return Double.compare(car1.getGasLevel(), car2.getGasLevel());
    }
}
```

### Chained Comparators
```java
// Sort by gas level, then by brand
Comparator<Car> multiComparator = new GasComparator()
    .thenComparing(Comparator.comparing(Car::getBrand));
```

