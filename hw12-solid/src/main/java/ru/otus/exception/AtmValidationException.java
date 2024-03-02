package ru.otus.exception;

public class AtmValidationException extends AtmException {
    public AtmValidationException() {
        super();
    }

    public AtmValidationException(String message) {
        super(message);
    }

    public AtmValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
