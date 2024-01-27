package ru.otus.exceptions;

public class AssertEqualsException extends AssertException {
    public AssertEqualsException() {
        super();
    }

    public AssertEqualsException(String message) {
        super(message);
    }

    public AssertEqualsException(String message, Throwable cause) {
        super(message, cause);
    }
}
