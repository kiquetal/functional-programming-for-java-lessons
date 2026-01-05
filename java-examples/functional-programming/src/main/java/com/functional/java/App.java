package com.functional.java;

import java.util.*;
import java.util.function.ToIntFunction;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        CarCriteria n = (Car c) -> {
            return c.getColor().equals("RED");
        };

        // how to use CarCriteria
        List<String> colors = Arrays.asList("RED", "GREEN", "BLUE");

        System.out.println(getAllCriterion(colors,  s -> s.equals("BLUE")));

        // Test showAll method with cars
        System.out.println("\n=== Testing showAll method ===");
        List<Car> cars = createTestCars();

        System.out.println("\nRed cars:");
        showAll(cars, getColorCriterion("RED"));

        System.out.println("\nRed or Green cars:");
        showAll(cars, getColorCriterion("RED", "GREEN"));

        System.out.println("\nNot Red cars:");
        showAll(cars, getInverse(getColorCriterion("RED")));

        System.out.println("\nToyota cars:");
        showAll(cars, c -> "Toyota".equals(c.getBrand()));

        System.out.println("\n=== Testing andCriterion method ===");
        System.out.println("\nRed AND Toyota cars:");
        showAll(cars, getColorCriterion("RED").andCriterion( c -> "Toyota".equals(c.getBrand())));

        System.out.println("\nRed AND BMW cars:");
        showAll(cars, getColorCriterion("RED").andCriterion(c -> "BMW".equals(c.getBrand())));

        System.out.println("\n=== Testing orCriterion method ===");
        System.out.println("\nRed OR Toyota cars:");
        showAll(cars, getColorCriterion("RED").orCriterion(c -> "Toyota".equals(c.getBrand())));

        System.out.println("\nBlue OR Green cars:");
        showAll(cars, getColorCriterion("BLUE").orCriterion(getColorCriterion("GREEN")));

        System.out.println("\n=== Testing combined criteria ===");
        System.out.println("\n(Red OR Blue) AND Toyota cars:");
        showAll(cars, getColorCriterion("RED").orCriterion(getColorCriterion("BLUE")).andCriterion(
            c -> "Toyota".equals(c.getBrand())
        ));

        System.out.println("\n=== Testing negate method ===");
        System.out.println("\nNOT Red cars (using Criterion.negate):");
        showAll(cars,getColorCriterion("RED").negate());

        System.out.println("\nNOT (Red OR Blue) cars:");
        showAll(cars, getColorCriterion("RED").orCriterion(getColorCriterion("BLUE")).negate());

        System.out.println("\n=== Testing negate with combined criteria ===");
        System.out.println("\nNOT (Red AND Toyota) - using negate:");
        showAll(cars, getColorCriterion("RED").andCriterion(c -> "Toyota".equals(c.getBrand())).negate());

        System.out.println("\n=== Testing GasComparator ===");
        System.out.println("\nCars sorted by gas level (ascending):");
        List<Car> sortedByGas = new ArrayList<>(cars);
        sortedByGas.sort(new GasComparator());
        sortedByGas.forEach(System.out::println);

        System.out.println("\nCars sorted by gas level (descending):");
        List<Car> sortedByGasDesc = new ArrayList<>(cars);
        sortedByGasDesc.sort(new GasComparator().reversed());
        sortedByGasDesc.forEach(System.out::println);

        System.out.println("\nCars with gas level > 10.0:");
        showAll(cars, c -> c.getGasLevel() > 10.0);

        System.out.println("\nCar with minimum gas level:");
        Car minGasCar = cars.stream().min(new GasComparator()).orElse(null);
        if (minGasCar != null) {
            System.out.println(minGasCar);
        }

        System.out.println("\nCar with maximum gas level:");
        Car maxGasCar = cars.stream().max(new GasComparator()).orElse(null);
        if (maxGasCar != null) {
            System.out.println(maxGasCar);
        }

        // ===== COMPARATOR EXPLANATION =====
        // Create Bert's car with ONLY 2.0 gallons of gas
        Car bertCar = new Car();
        bertCar.setBrand("something look");
        bertCar.setGasLevel(2);  // Bert has very little gas!

        GasComparator gasComparator = new GasComparator();

        // Create a function that compares ANY car with Bert's car
        // This "locks in" Bert's car as the second parameter
        ToIntFunction<Car> compareWithBert = compareWithThis(gasComparator, bertCar);

        System.out.println("\n=== BERT COMPARISON ===");
        System.out.println("Bert's car gas level: " + bertCar.getGasLevel());
        System.out.println("\nComparing each car with Bert's car:");
        System.out.println("Result meanings: -1 (less gas), 0 (equal gas), 1 (more gas)");
        System.out.println();

        // Iterate through cars and compare each with Bert
        // WHY DO WE GET 1 FOR ALL CARS?
        // Because ALL test cars have MORE gas than Bert's 2.0 gallons!
        // - RED Toyota: 15.5 > 2.0 → returns 1
        // - BLUE Honda: 8.2 > 2.0 → returns 1
        // - GREEN Ford: 20.0 > 2.0 → returns 1
        // - RED BMW: 5.5 > 2.0 → returns 1
        // - WHITE Toyota: 12.0 > 2.0 → returns 1
        cars.forEach(car -> {
            int result = compareWithBert.applyAsInt(car);
            String comparison = result > 0 ? "MORE" : result < 0 ? "LESS" : "EQUAL";
            System.out.println(car.getBrand() + " (" + car.getGasLevel() + " gas) vs Bert (2.0 gas) = "
                + result + " [" + comparison + " gas than Bert]");
        });

        // ===== DEMONSTRATE ALL THREE COMPARISON RESULTS =====
        System.out.println("\n=== Demonstrating All Comparison Results ===");

        // Create test cars with different gas levels relative to Bert (2.0)
        Car lessGasCar = new Car();
        lessGasCar.setBrand("Empty Tank");
        lessGasCar.setGasLevel(1.0);  // Less than Bert's 2.0

        Car sameGasCar = new Car();
        sameGasCar.setBrand("Same as Bert");
        sameGasCar.setGasLevel(2.0);  // Equal to Bert's 2.0

        Car moreGasCar = new Car();
        moreGasCar.setBrand("Full Tank");
        moreGasCar.setGasLevel(15.0);  // More than Bert's 2.0

        System.out.println("Empty Tank (1.0) vs Bert (2.0) = "
            + compareWithBert.applyAsInt(lessGasCar) + " ← LESS gas (-1)");
        System.out.println("Same as Bert (2.0) vs Bert (2.0) = "
            + compareWithBert.applyAsInt(sameGasCar) + " ← EQUAL gas (0)");
        System.out.println("Full Tank (15.0) vs Bert (2.0) = "
            + compareWithBert.applyAsInt(moreGasCar) + " ← MORE gas (1)");

    }

    /**
     * Creates a ToIntFunction that compares any value with a fixed target using a comparator.
     * This is a "partial application" pattern - we "lock in" the target parameter.
     *
     * @param comparator The comparator to use for comparison
     * @param target The fixed value to compare against (e.g., Bert's car)
     * @return A function that takes ONE value and compares it with the target
     *
     * Example: If target has gasLevel = 2.0
     *   - Input car with gas 15.5 → returns 1 (more than target)
     *   - Input car with gas 2.0 → returns 0 (equal to target)
     *   - Input car with gas 1.0 → returns -1 (less than target)
     */
    private static <E> ToIntFunction<E> compareWithThis(
            Comparator<E> comparator,
            E target
    ) {
        // Returns: comparator.compare(inputValue, target)
        // The target is "captured" in the lambda closure
        return value -> comparator.compare(value, target);
    }
    private static Criterion<Car> getColorCriterion(String... colors) {
       //implement the Criterion for this color

        return c -> Arrays.stream(colors).anyMatch(color -> color.equals(c.getColor()));
    }

    private static List<Car> createTestCars() {
        List<Car> cars = new ArrayList<>();

        Car car1 = new Car();
        car1.setColor("RED");
        car1.setBrand("Toyota");
        car1.setGasLevel(15.5);

        Car car2 = new Car();
        car2.setColor("BLUE");
        car2.setBrand("Honda");
        car2.setGasLevel(8.2);

        Car car3 = new Car();
        car3.setColor("GREEN");
        car3.setBrand("Ford");
        car3.setGasLevel(20.0);

        Car car4 = new Car();
        car4.setColor("RED");
        car4.setBrand("BMW");
        car4.setGasLevel(5.5);

        Car car5 = new Car();
        car5.setColor("WHITE");
        car5.setBrand("Toyota");
        car5.setGasLevel(12.0);

        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);
        cars.add(car5);

        return cars;
    }

  public  static <E> List<E> getAllCriterion(Iterable<E> inList, Criterion<E> crit) {
        List<E> list = new ArrayList<>();

        for (E e : inList)
        {
            if (crit.test(e))
            {
                list.add(e);
            }

        }
        return list;
  }

    //create a method to return a criterion based on colors



    public static Criterion<Car> getInverse(Criterion<Car> crit) {

        return e -> !crit.test(e);

    }



    public static <E> void showAll(List<E> list, Criterion<E> criterion) {
        for (E element : list) {
            if (criterion.test(element)) {
                System.out.println(element);
            }
        }
    }

    public class CarScratch {
        public static <E> Criterion<E> negate(Criterion<E> crit) {
            return  x -> !crit.test(x);
        }
    }
}





interface CarCriteria {

    public boolean test( Car c);
}

class Car {

    private String color;
    private String brand;
    private double gasLevel; // Gas level in gallons or liters

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public double getGasLevel()
    {
        return gasLevel;
    }

    public void setGasLevel(double gasLevel)
    {
        this.gasLevel = gasLevel;
    }

    @Override
    public String toString() {
        return "Car{color='" + color + "', brand='" + brand + "', gasLevel=" + gasLevel + "}";
    }

}
