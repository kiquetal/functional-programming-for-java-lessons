package com.functional.java;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    private List<Car> testCars;

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testCars = createTestCars();
    }

    private List<Car> createTestCars() {
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

    /**
     * Test showAll method with color criterion
     */
    public void testShowAllWithColorCriterion() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        App.showAll(testCars, App.getColorCriterion("RED"));

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue("Output should contain RED Toyota car", output.contains("RED") && output.contains("Toyota"));
        assertTrue("Output should contain RED BMW car", output.contains("RED") && output.contains("BMW"));
        assertFalse("Output should not contain BLUE cars", output.contains("BLUE"));
    }

    /**
     * Test showAll method with brand criterion
     */
    public void testShowAllWithBrandCriterion() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        App.showAll(testCars, (Car c) -> "Toyota".equals(c.getBrand()));

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue("Output should contain Toyota cars", output.contains("Toyota"));
        // Should have 2 Toyota cars (RED and WHITE)
        int toyotaCount = output.split("Toyota").length - 1;
        assertEquals("Should have 2 Toyota cars", 2, toyotaCount);
    }

    /**
     * Test andCriterion with color and brand
     */
    public void testAndCriterion() {
        Criterion<Car> redAndToyota = App.andCriterion(
            App.getColorCriterion("RED"),
            c -> "Toyota".equals(c.getBrand())
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (redAndToyota.test(car)) {
                result.add(car);
            }
        }

        assertEquals("Should have exactly 1 RED Toyota car", 1, result.size());
        assertEquals("Car should be RED", "RED", result.get(0).getColor());
        assertEquals("Car should be Toyota", "Toyota", result.get(0).getBrand());
    }

    /**
     * Test andCriterion with no matches
     */
    public void testAndCriterionNoMatch() {
        Criterion<Car> blueAndToyota = App.andCriterion(
            App.getColorCriterion("BLUE"),
            c -> "Toyota".equals(c.getBrand())
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (blueAndToyota.test(car)) {
                result.add(car);
            }
        }

        assertEquals("Should have no BLUE Toyota cars", 0, result.size());
    }

    /**
     * Test orCriterion with color and brand
     */
    public void testOrCriterion() {
        Criterion<Car> redOrToyota = App.orCriterion(
            App.getColorCriterion("RED"),
            c -> "Toyota".equals(c.getBrand())
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (redOrToyota.test(car)) {
                result.add(car);
            }
        }

        // Should have: RED Toyota, RED BMW, WHITE Toyota = 3 cars
        assertEquals("Should have 3 cars (RED Toyota, RED BMW, WHITE Toyota)", 3, result.size());
    }

    /**
     * Test orCriterion with two colors
     */
    public void testOrCriterionTwoColors() {
        Criterion<Car> blueOrGreen = App.orCriterion(
            App.getColorCriterion("BLUE"),
            App.getColorCriterion("GREEN")
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (blueOrGreen.test(car)) {
                result.add(car);
            }
        }

        assertEquals("Should have 2 cars (BLUE and GREEN)", 2, result.size());
    }

    /**
     * Test combined andCriterion and orCriterion
     */
    public void testCombinedCriteria() {
        // (RED OR BLUE) AND Toyota
        Criterion<Car> combined = App.andCriterion(
            App.orCriterion(App.getColorCriterion("RED"), App.getColorCriterion("BLUE")),
            c -> "Toyota".equals(c.getBrand())
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (combined.test(car)) {
                result.add(car);
            }
        }

        // Should have only RED Toyota (no BLUE Toyota exists)
        assertEquals("Should have 1 car (RED Toyota)", 1, result.size());
        assertEquals("Car should be RED", "RED", result.get(0).getColor());
        assertEquals("Car should be Toyota", "Toyota", result.get(0).getBrand());
    }

    /**
     * Test Criterion.negate with color criterion
     */
    public void testNegateColorCriterion() {
        Criterion<Car> notRed = Criterion.negate(App.getColorCriterion("RED"));

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (notRed.test(car)) {
                result.add(car);
            }
        }

        // Should have 3 non-RED cars (BLUE, GREEN, WHITE)
        assertEquals("Should have 3 non-RED cars", 3, result.size());
        for (Car car : result) {
            assertFalse("Car should not be RED", "RED".equals(car.getColor()));
        }
    }

    /**
     * Test Criterion.negate with brand criterion
     */
    public void testNegateBrandCriterion() {
        Criterion<Car> notToyota = Criterion.negate(c -> "Toyota".equals(c.getBrand()));

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (notToyota.test(car)) {
                result.add(car);
            }
        }

        // Should have 3 non-Toyota cars (Honda, Ford, BMW)
        assertEquals("Should have 3 non-Toyota cars", 3, result.size());
        for (Car car : result) {
            assertFalse("Car should not be Toyota", "Toyota".equals(car.getBrand()));
        }
    }

    /**
     * Test Criterion.negate with combined criteria
     */
    public void testNegateCombinedCriteria() {
        // NOT (RED OR BLUE)
        Criterion<Car> notRedOrBlue = Criterion.negate(
            App.orCriterion(App.getColorCriterion("RED"), App.getColorCriterion("BLUE"))
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (notRedOrBlue.test(car)) {
                result.add(car);
            }
        }

        // Should have 2 cars (GREEN and WHITE)
        assertEquals("Should have 2 cars that are neither RED nor BLUE", 2, result.size());
        for (Car car : result) {
            assertFalse("Car should not be RED", "RED".equals(car.getColor()));
            assertFalse("Car should not be BLUE", "BLUE".equals(car.getColor()));
        }
    }

    /**
     * Test Criterion.negate with andCriterion
     */
    public void testNegateAndCriterion() {
        // NOT (RED AND Toyota)
        Criterion<Car> notRedAndToyota = Criterion.negate(
            App.andCriterion(App.getColorCriterion("RED"), c -> "Toyota".equals(c.getBrand()))
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (notRedAndToyota.test(car)) {
                result.add(car);
            }
        }

        // Should have 4 cars (all except RED Toyota)
        assertEquals("Should have 4 cars (all except RED Toyota)", 4, result.size());

        // Verify RED Toyota is not in the results
        for (Car car : result) {
            boolean isRedToyota = "RED".equals(car.getColor()) && "Toyota".equals(car.getBrand());
            assertFalse("Should not contain RED Toyota", isRedToyota);
        }
    }

    /**
     * Test showAll with negate criterion
     */
    public void testShowAllWithNegate() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        App.showAll(testCars, Criterion.negate(App.getColorCriterion("RED")));

        System.setOut(originalOut);
        String output = outContent.toString();

        assertFalse("Output should not contain RED cars", output.contains("RED"));
        assertTrue("Output should contain other colors",
            output.contains("BLUE") || output.contains("GREEN") || output.contains("WHITE"));
    }

    /**
     * Test double negation
     */
    public void testDoubleNegation() {
        // NOT NOT RED should be same as RED
        Criterion<Car> doubleNegateRed = Criterion.negate(
            Criterion.negate(App.getColorCriterion("RED"))
        );

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (doubleNegateRed.test(car)) {
                result.add(car);
            }
        }

        // Should have 2 RED cars
        assertEquals("Should have 2 RED cars", 2, result.size());
        for (Car car : result) {
            assertEquals("Car should be RED", "RED", car.getColor());
        }
    }

    /**
     * Test showAll with andCriterion
     */
    public void testShowAllWithAndCriterion() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        App.showAll(testCars, App.andCriterion(
            App.getColorCriterion("RED"),
            c -> "BMW".equals(c.getBrand())
        ));

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue("Output should contain RED BMW car", output.contains("RED") && output.contains("BMW"));
        assertFalse("Output should not contain Toyota", output.contains("Toyota"));
    }

    /**
     * Test showAll with orCriterion
     */
    public void testShowAllWithOrCriterion() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        App.showAll(testCars, App.orCriterion(
            App.getColorCriterion("BLUE"),
            App.getColorCriterion("GREEN")
        ));

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue("Output should contain BLUE car", output.contains("BLUE"));
        assertTrue("Output should contain GREEN car", output.contains("GREEN"));
        assertFalse("Output should not contain RED", output.contains("RED"));
    }

    /**
     * Test getColorCriterion with multiple colors
     */
    public void testGetColorCriterionMultipleColors() {
        Criterion<Car> redOrGreen = App.getColorCriterion("RED", "GREEN");

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (redOrGreen.test(car)) {
                result.add(car);
            }
        }

        assertEquals("Should have 3 cars (2 RED and 1 GREEN)", 3, result.size());
    }

    /**
     * Test getInverse criterion
     */
    public void testGetInverseCriterion() {
        Criterion<Car> notRed = App.getInverse(App.getColorCriterion("RED"));

        List<Car> result = new ArrayList<>();
        for (Car car : testCars) {
            if (notRed.test(car)) {
                result.add(car);
            }
        }

        // Should have 3 non-RED cars (BLUE, GREEN, WHITE)
        assertEquals("Should have 3 non-RED cars", 3, result.size());
        for (Car car : result) {
            assertFalse("Car should not be RED", "RED".equals(car.getColor()));
        }
    }

    /**
     * Test getAllCriterion generic method
     */
    public void testGetAllCriterion() {
        List<String> colors = Arrays.asList("RED", "GREEN", "BLUE");
        List<String> result = App.getAllCriterion(colors, s -> s.equals("BLUE"));

        assertEquals("Should have 1 matching color", 1, result.size());
        assertEquals("Should be BLUE", "BLUE", result.get(0));
    }

    /**
     * Test showAll with empty list
     */
    public void testShowAllWithEmptyList() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        App.showAll(new ArrayList<Car>(), App.getColorCriterion("RED"));

        System.setOut(originalOut);
        String output = outContent.toString();

        assertEquals("Output should be empty", "", output);
    }
}
