package com.functional.java;

import java.util.Comparator;

/**
 * Comparator for comparing Cars based on their gas level.
 * Cars with higher gas levels are considered "greater".
 */
public class GasComparator implements Comparator<Car> {

    @Override
    public int compare(Car car1, Car car2) {
        // Compare gas levels
        return Double.compare(car1.getGasLevel(),   car2.getGasLevel());
    }
}

