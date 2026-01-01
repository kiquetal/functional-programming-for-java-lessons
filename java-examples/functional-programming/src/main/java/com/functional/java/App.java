package com.functional.java;

import java.util.*;

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

    static void executeCriteria(CarCriteria n) {
    System.out.println("Execute");
    Car c = new Car();
    System.out.println(n.test(c));
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

    public static Criterion<Car> getColorCriterion(String... colors)
    {
        Set<String> colorsSet = new HashSet<>(Arrays.asList(colors));
        return c ->
                colorsSet.contains(c.getColor());
    }

    public static Criterion<Car> getInverse(Criterion<Car> crit) {

        return e -> !crit.test(e);

    }

    public static Criterion<Car> andCriterion(Criterion<Car> crit1, Criterion<Car> crit2) {
        return c -> crit1.test(c) && crit2.test(c);
    }

    public static Criterion<Car> orCriterion(Criterion<Car> crit1, Criterion<Car> crit2) {
        return c -> crit1.test(c) || crit2.test(c);
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




@FunctionalInterface
interface Criterion<E> {
    boolean test(E e);
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
