package com.functional.java;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class MethodReferenceExample {

    static class Person {
        String name;
        Person(String name) { this.name = name; }
        String getName() { return name; }
        @Override
        public String toString() { return name; }
    }

    public static int compareLength(String s1, String s2) {
        return Integer.compare(s1.length(), s2.length());
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dave");

        System.out.println("--- 1. Static Method Reference ---");
        // Using static method compareLength
        names.sort(MethodReferenceExample::compareLength);
        System.out.println("Sorted by length: " + names);

        System.out.println("\n--- 2. Instance Method Reference (Particular Object) ---");
        // Using System.out::println
        names.forEach(System.out::println);

        System.out.println("\n--- 3. Instance Method Reference (Arbitrary Object of Type) ---");
        // Using String::toUpperCase
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Uppercase: " + upperNames);

        System.out.println("\n--- 4. Constructor Reference ---");
        // Using Person::new
        List<Person> people = names.stream()
            .map(Person::new)
            .collect(Collectors.toList());
        System.out.println("People objects: " + people);
        
        // Using ArrayList::new
        List<Person> peopleCopy = people.stream()
            .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("People copy (ArrayList): " + peopleCopy);
    }
}
