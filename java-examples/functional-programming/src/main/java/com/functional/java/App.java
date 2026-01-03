package com.functional.java;

import java.util.*;

import static com.functional.java.Criterion.andCriterion;
import static com.functional.java.Criterion.orCriterion;

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
        showAll(cars, andCriterion(getColorCriterion("RED"), c -> "Toyota".equals(c.getBrand())));

        System.out.println("\nRed AND BMW cars:");
        showAll(cars, andCriterion(getColorCriterion("RED"), c -> "BMW".equals(c.getBrand())));

        System.out.println("\n=== Testing orCriterion method ===");
        System.out.println("\nRed OR Toyota cars:");
        showAll(cars, orCriterion(getColorCriterion("RED"), c -> "Toyota".equals(c.getBrand())));

        System.out.println("\nBlue OR Green cars:");
        showAll(cars, orCriterion(getColorCriterion("BLUE"), getColorCriterion("GREEN")));

        System.out.println("\n=== Testing combined criteria ===");
        System.out.println("\n(Red OR Blue) AND Toyota cars:");
        showAll(cars, andCriterion(
            orCriterion(getColorCriterion("RED"), getColorCriterion("BLUE")),
            c -> "Toyota".equals(c.getBrand())
        ));

        System.out.println("\n=== Testing negate method ===");
        System.out.println("\nNOT Red cars (using Criterion.negate):");
        showAll(cars, Criterion.negate(getColorCriterion("RED")));

        System.out.println("\nNOT Toyota cars:");
        showAll(cars, Criterion.negate(c -> "Toyota".equals(c.getBrand())));

        System.out.println("\nNOT (Red OR Blue) cars:");
        showAll(cars, Criterion.negate(orCriterion(getColorCriterion("RED"), getColorCriterion("BLUE"))));

        System.out.println("\n=== Testing negate with combined criteria ===");
        System.out.println("\nNOT (Red AND Toyota) - using negate:");
        showAll(cars, Criterion.negate(andCriterion(getColorCriterion("RED"), c -> "Toyota".equals(c.getBrand()))));

    }

    private static Criterion<Car> getColorCriterion(String... colors) {
       //implement the Criterion for this color

        return c -> {

            return  Arrays.stream(colors).anyMatch(color -> color.equals(c.getColor()));
        };
    }

    private static List<Car> createTestCars() {
        List<Car> cars = new ArrayList<>();

        Car car1 = new Car();
        car1.setColor("RED");
        car1.setBrand("Toyota");

        Car car2 = new Car();
        car2.setColor("BLUE");
        car2.setBrand("Honda");

        Car car3 = new Car();
        car3.setColor("GREEN");
        car3.setBrand("Ford");

        Car car4 = new Car();
        car4.setColor("RED");
        car4.setBrand("BMW");

        Car car5 = new Car();
        car5.setColor("WHITE");
        car5.setBrand("Toyota");

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

    private String brand;

    @Override
    public String toString() {
        return "Car{color='" + color + "', brand='" + brand + "'}";
    }

}
