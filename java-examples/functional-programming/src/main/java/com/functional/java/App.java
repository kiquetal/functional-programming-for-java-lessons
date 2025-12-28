package com.functional.java;

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
        }
    }
}

interface CarCrieria {

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
