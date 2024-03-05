package ru.otus.exception;

@SuppressWarnings({"java:S2166"})
public class EvenSecondException extends RuntimeException {
    public EvenSecondException() {
        super();
    }

    public EvenSecondException(String message) {
        super(message);
    }

    public EvenSecondException(String message, Throwable cause) {
        super(message, cause);
    }
}
