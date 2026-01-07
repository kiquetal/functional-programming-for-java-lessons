package com.functional.java;

import java.util.Arrays;
import java.util.List;

public class PreservingContextExample {

    public static void main(String[] args) {
        // Setup data
        User alice = new User("Alice", Arrays.asList("alice@work.com", "alice@home.com"));
        User bob = new User("Bob", Arrays.asList("bob@gmail.com"));
        User charlie = new User("Charlie", Arrays.asList()); // No emails

        List<User> userList = Arrays.asList(alice, bob, charlie);
        SuperIterable<User> users = new SuperIterable<>(userList);

        System.out.println("=== 1. The Problem: Losing Boundaries ===");
        System.out.println("When we flatMap to just emails, we lose the association with the User.");
        
        users.flatMap(user -> new SuperIterable<>(user.getEmails()))
             .forEvery(email -> System.out.println("Email: " + email));

        System.out.println("\n=== 2. The Solution: Preserving Context ===");
        System.out.println("We can map the inner iterable to hold both the user and the email before flattening.");
        
        // Use SuperIterable as is: 
        // Inside flatMap, we turn the list of emails into a SuperIterable, 
        // then MAP it to include the User context.
        users.flatMap(user -> 
            new SuperIterable<>(user.getEmails())
                .map(email -> "User: " + user.getName() + " -> Email: " + email)
        )
        .forEvery(result -> System.out.println(result));
    }

    static class User {
        private String name;
        private List<String> emails;

        public User(String name, List<String> emails) {
            this.name = name;
            this.emails = emails;
        }

        public String getName() {
            return name;
        }

        public List<String> getEmails() {
            return emails;
        }
    }
}
