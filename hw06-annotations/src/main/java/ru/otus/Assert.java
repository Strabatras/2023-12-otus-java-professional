package ru.otus;

import ru.otus.exceptions.AssertEqualsException;

public class Assert {
    private static final String MESSAGE_EQUALS_EXCEPTION = "Checked values are not equal";

    private Assert() {}

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertEqualsException(MESSAGE_EQUALS_EXCEPTION);
        }
    }

    public static void assertEquals(String expected, String actual) {
        if (expected == null || !expected.equals(actual)) {
            throw new AssertEqualsException(MESSAGE_EQUALS_EXCEPTION);
        }
    }
}
