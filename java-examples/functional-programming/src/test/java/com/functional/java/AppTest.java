package com.functional.java;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.List;
import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testCarFactory()
    {
        Car car = Car.withGasColorPassengers(10.0, "Red", "Toyota", "Alice", "Bob");
        assertEquals(10.0, car.getGasLevel(), 0.001);
        assertEquals("Red", car.getColor());
        assertEquals("Toyota", car.getBrand());
        assertEquals(2, car.getPassengers().size());
        assertEquals("Alice", car.getPassengers().get(0));
        assertEquals("Bob", car.getPassengers().get(1));
        assertNotNull(car.getTrunk());
        assertEquals(0, car.getTrunk().size());

        String[] trunkItems = {"Spare Tire", "Jack"};
        String[] passengers = {"Charlie"};
        Car carWithTrunk = Car.withGasColorPassengersAndTrunk(15.0, "Blue", "Honda", passengers, trunkItems);
        assertEquals(15.0, carWithTrunk.getGasLevel(), 0.001);
        assertEquals("Blue", carWithTrunk.getColor());
        assertEquals("Honda", carWithTrunk.getBrand());
        assertEquals(1, carWithTrunk.getPassengers().size());
        assertEquals("Charlie", carWithTrunk.getPassengers().get(0));
        assertEquals(2, carWithTrunk.getTrunk().size());
        assertEquals("Spare Tire", carWithTrunk.getTrunk().get(0));
    }
}