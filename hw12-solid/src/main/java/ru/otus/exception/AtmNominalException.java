package ru.otus.exception;

public class AtmNominalException extends AtmException {
    public AtmNominalException() {
        super();
    }

    public AtmNominalException(String message) {
        super(message);
    }

    public AtmNominalException(String message, Throwable cause) {
        super(message, cause);
    }
}
