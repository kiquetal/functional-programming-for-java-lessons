package com.functional.java;

import java.util.Arrays;
import java.util.List;

public class MethodReferenceDifference {

    public static void main(String[] args) {
        System.out.println("=== Method Reference Differences ===");

        // --- TYPE 2: Reference to an Instance Method of a Particular Object ---
        // Context: 'printer' is a SPECIFIC object that already exists.
        // We want to use THIS specific object to process elements.
        Printer printer = new Printer();
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        System.out.println("\n1. Instance Method of a Particular Object (printer::print)");
        // Syntax: containingObject::instanceMethodName
        // Lambda equivalent: names.forEach(name -> printer.print(name));
        names.forEach(printer::print);


        // --- TYPE 3: Reference to an Instance Method of an Arbitrary Object of a Particular Type ---
        // Context: The method we want to call belongs to the objects IN the list itself.
        // We don't have a single external object doing the work; each element does the work on itself.
        List<String> cities = Arrays.asList("new york", "london", "paris");

        System.out.println("\n2. Instance Method of an Arbitrary Object of a Particular Type (String::toUpperCase)");
        // Syntax: ClassName::instanceMethodName
        // Lambda equivalent: cities.stream().map(city -> city.toUpperCase())...
        cities.stream()
              .map(String::toUpperCase) // 'toUpperCase' is called ON the string element
              .forEach(System.out::println);
    }

    static class Printer {
        public void print(String s) {
            System.out.println("Printer object says: " + s);
        }
    }
}
