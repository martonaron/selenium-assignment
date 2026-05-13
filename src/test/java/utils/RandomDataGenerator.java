package utils;

import java.util.UUID;

/**
 * Generates randomized test data to avoid collisions between test runs.
 * Covers: random_data advanced task.
 */
public class RandomDataGenerator {

    private RandomDataGenerator() {
    }

    public static String generateEmail() {
        return "test." + UUID.randomUUID().toString().substring(0, 8) + "@spree.test";
    }

    public static String generateFirstName() {
        String[] names = { "Alice", "Bob", "Carol", "Dave", "Eve", "Frank" };
        return names[(int) (Math.random() * names.length)];
    }

    public static String generateLastName() {
        String[] surnames = { "Smith", "Jones", "Brown", "Wilson", "Taylor", "Davis" };
        return surnames[(int) (Math.random() * surnames.length)];
    }

    public static String generatePassword() {
        return "Pass_" + UUID.randomUUID().toString().substring(0, 8) + "!";
    }
}
