package ru.otus.util;

import ru.otus.exception.EvenSecondException;

@SuppressWarnings("java:S106")
public class ExceptionUtil {
    private ExceptionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void complexProcessorExceptionHandler(Exception ex) {
        if (ex instanceof EvenSecondException) {
            System.out.println("Processor returned exception because value is " + ex.getMessage());
        }
    }
}
