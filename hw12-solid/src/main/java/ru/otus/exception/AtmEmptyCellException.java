package ru.otus.exception;

public class AtmEmptyCellException extends RuntimeException {
    public AtmEmptyCellException() {
        super();
    }

    public AtmEmptyCellException(String message) {
        super(message);
    }

    public AtmEmptyCellException(String message, Throwable cause) {
        super(message, cause);
    }
}
