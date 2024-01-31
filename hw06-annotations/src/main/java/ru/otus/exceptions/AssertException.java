package ru.otus.exceptions;

public class AssertException extends RuntimeException {
    public AssertException() {
        super();
    }

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }
}
