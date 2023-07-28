package com.dsluchenko.app.service.exception;

public class UnavailableExchangeException extends RuntimeException {
    private final static String message = "Exchange rate in not available";

    public UnavailableExchangeException() {
        super(message);
    }

    public UnavailableExchangeException(String message) {
        super(message);
    }

    public UnavailableExchangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableExchangeException(Throwable cause) {
        super(cause);
    }
}
