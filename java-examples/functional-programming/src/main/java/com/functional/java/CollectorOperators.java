package com.functional.java;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;

public class CollectorOperators {

    static class User {
        private String name;
        private String city;
        private int age;

        public User(String name, String city, int age) {
            this.name = name;
            this.city = city;
            this.age = age;
        }

        public String getName() { return name; }
        public String getCity() { return city; }
        public int getAge() { return age; }
        
        @Override
        public String toString() { return name + "(" + age + ")"; }
    }

    public static void main(String[] args) {
        System.out.println("=== 1. Map Sorting Examples ===");
        
        Map<Integer, User> userMap = new HashMap<>();
        userMap.put(101, new User("Alice", "London", 30));
        userMap.put(103, new User("Charlie", "New York", 25));
        userMap.put(102, new User("Bob", "London", 35));

        System.out.println("Original Map: " + userMap);

        // Sort by User Name (Value)
        Map<Integer, User> sortedByName = userMap.entrySet().stream()
            .sorted(Entry.comparingByValue(Comparator.comparing(User::getName)))
            .collect(Collectors.toMap(
                Entry::getKey,
                Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        
        System.out.println("Sorted by Name: " + sortedByName);

        // Sort by ID (Key) descending
        Map<Integer, User> sortedByIdDesc = userMap.entrySet().stream()
            .sorted(Entry.comparingByKey(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                Entry::getKey,
                Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        System.out.println("Sorted by ID (Desc): " + sortedByIdDesc);

        System.out.println("\n=== 2. GroupingBy Examples ===");
        
        List<User> userList = Arrays.asList(
            new User("Alice", "London", 30),
            new User("Charlie", "New York", 25),
            new User("Bob", "London", 35),
            new User("Dave", "New York", 40)
        );

        // Group by City
        Map<String, List<User>> usersByCity = userList.stream()
            .collect(Collectors.groupingBy(User::getCity));
        
        System.out.println("Users grouped by City: " + usersByCity);

        // Group by City, counting users
        Map<String, Long> countByCity = userList.stream()
            .collect(Collectors.groupingBy(User::getCity, Collectors.counting()));
            
        System.out.println("Count by City: " + countByCity);

        System.out.println("\n=== 3. Joining Examples ===");
        
        // Simple Joining
        String names = userList.stream()
            .map(User::getName)
            .collect(Collectors.joining(", "));
        System.out.println("Joined Names: " + names);

        // Joining with Prefix and Suffix
        String namesWithBrackets = userList.stream()
            .map(User::getName)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Joined Names with brackets: " + namesWithBrackets);
    }
}
