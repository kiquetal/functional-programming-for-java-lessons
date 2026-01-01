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

}
