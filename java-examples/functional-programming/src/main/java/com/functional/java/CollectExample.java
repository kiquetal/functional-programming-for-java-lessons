package com.functional.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Demonstrates the use of the 3-argument version of collect():
 * collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner)
 * 
 * This version is the "manual" way to perform a mutable reduction.
 */
public class CollectExample {

    public static void main(String[] args) {
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");

        System.out.println("=== Example 1: Basic 3-argument collect ===");
        // Manual collection into an ArrayList
        List<String> list = words.stream()
            .filter(s -> s.length() > 5)
            .map(String::toUpperCase)
            .collect(
                () -> new ArrayList<>(),     // 1. Supplier: How to create the result container
                (acc, item) -> acc.add(item), // 2. Accumulator: How to add an element to the container
                (list1, list2) -> list1.addAll(list2) // 3. Combiner: How to merge two containers (for parallel streams)
            );
        System.out.println("Filtered and Mapped List: " + list);

        System.out.println("\n=== Example 2: Collecting into a StringBuilder ===");
        // Manual collection into a StringBuilder with a delimiter
        StringBuilder csv = words.stream()
            .map(String::toLowerCase)
            .collect(
                StringBuilder::new,           // Supplier
                (sb, s) -> {                  // Accumulator
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(s);
                },
                (sb1, sb2) -> {               // Combiner (merging two StringBuilders)
                    if (sb1.length() > 0 && sb2.length() > 0) sb1.append(", ");
                    sb1.append(sb2);
                }
            );
        System.out.println("CSV String: " + csv.toString());

        System.out.println("\n=== Example 3: Complex object accumulation ===");
        // Collecting into a custom "Stats" object
        class WordStats {
            int totalLength = 0;
            int wordCount = 0;
            void add(String s) { totalLength += s.length(); wordCount++; }
            void merge(WordStats other) { totalLength += other.totalLength; wordCount += other.wordCount; }
            @Override
            public String toString() { return "Avg length: " + (wordCount == 0 ? 0 : (double)totalLength/wordCount); }
        }

        WordStats stats = words.stream()
            .collect(
                WordStats::new,
                WordStats::add,
                WordStats::merge
            );
        System.out.println("Stats: " + stats);

        System.out.println("\n=== Example 4: Parallel Stream execution ===");
        // The combiner is actually used here
        List<String> parallelList = words.parallelStream()
            .collect(
                ArrayList::new,
                ArrayList::add,
                ArrayList::addAll
            );
        System.out.println("Parallel collected list: " + parallelList);
    }
}
