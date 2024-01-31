package ru.otus.exceptions;

public class BeforeTestException extends RuntimeException {
    public BeforeTestException() {
        super();
    }

    public BeforeTestException(String message) {
        super(message);
    }

    public BeforeTestException(String message, Throwable cause) {
        super(message, cause);
    }
}
