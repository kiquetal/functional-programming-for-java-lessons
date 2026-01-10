package com.functional.java;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlatMapExamples {

    public static void main(String[] args) {
        System.out.println("=== 1. Collection Flattening with SuperIterable ===");
        // Scenario: Each number 'n' is mapped to a SuperIterable containing [n, n+1]
        // Without flatMap, we'd get SuperIterable<SuperIterable<Integer>>
        // With flatMap, we get SuperIterable<Integer>
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        SuperIterable<Integer> superNumbers = new SuperIterable<>(numbers);

        System.out.println("Mapping 1 -> [1, 2], 2 -> [2, 3], 3 -> [3, 4] and flattening:");
        superNumbers
            .flatMap(n -> new SuperIterable<>(Arrays.asList(n, n + 1)))
            .forEvery(n -> System.out.print(n + " ")); // Output: 1 2 2 3 3 4
        System.out.println("\n");


        System.out.println("=== 2. Nested Lists with Java Streams ===");
        // Scenario: A list of orders, where each order has a list of items.
        // We want a single list of ALL items across all orders.
        List<List<String>> orders = Arrays.asList(
            Arrays.asList("Laptop", "Mouse"),
            Arrays.asList("Keyboard", "Monitor"),
            Arrays.asList("Headphones")
        );

        List<String> allItems = orders.stream()
            .flatMap(order -> order.stream()) // Flattens List<List<String>> to Stream<String>
            .collect(Collectors.toList());

        System.out.println("All items from all orders: " + allItems);
        System.out.println();


        System.out.println("=== 3. Avoiding Nested Optionals ===");
        // Scenario: A person might have a car, and a car might have an insurance policy.
        // Both can be missing (Optional).
        Person person = new Person(new Car("Toyota"));
        Person personWithoutCar = new Person(null);

        // BAD: using map leads to Optional<Optional<String>>
        Optional<Optional<String>> nested = person.getCar()
            .map(car -> car.getInsuranceIdentifier());

        // GOOD: using flatMap flattens the result to Optional<String>
        String insuranceId = person.getCar()
            .flatMap(car -> car.getInsuranceIdentifier())
            .orElse("No Insurance");

        System.out.println("Person's car insurance: " + insuranceId);

        String noCarInsurance = personWithoutCar.getCar()
            .flatMap(car -> car.getInsuranceIdentifier())
            .orElse("No Insurance");
        System.out.println("Person without car insurance: " + noCarInsurance);

        System.out.println("\n");
        System.out.println("=== 4. Stream of Objects with Optionals (Map + orElse) ===");
        // Scenario: We have a list of Users. Some have nicknames, some don't.
        // We want a list of ALL nicknames, using "Anonymous" for missing ones.
        
        List<User> users = Arrays.asList(
            new User("Alice", "Ally"),
            new User("Bob", null),
            new User("Charlie", "Chuck")
        );

        List<String> nicknames = users.stream()
            .map(user -> user.getNickname().orElse("Anonymous"))
            .collect(Collectors.toList());

        System.out.println("All nicknames (including defaults): " + nicknames);
    }

    // Helper classes for the Optional example
    static class Person {
        private Car car;
        public Person(Car car) { this.car = car; }
        public Optional<Car> getCar() { return Optional.ofNullable(car); }
    }

    static class Car {
        private String model;
        private String insuranceId; // Could be null

        public Car(String model) {
            this.model = model;
            if ("Toyota".equals(model)) this.insuranceId = "TOY-123-INS";
        }

        public Optional<String> getInsuranceIdentifier() {
            return Optional.ofNullable(insuranceId);
        }
    }

    static class User {
        private String name;
        private String nickname;

        public User(String name, String nickname) {
            this.name = name;
            this.nickname = nickname;
        }

        public Optional<String> getNickname() {
            return Optional.ofNullable(nickname);
        }
    }
}
