package com.dsluchenko.app.service.exception;

public class CurrencyNotFoundException extends RuntimeException {
    private static final String message = "Currency not found";

    public CurrencyNotFoundException() {
        super(message);
    }

    public CurrencyNotFoundException(String message) {
        super(message);
    }

    public CurrencyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyNotFoundException(Throwable cause) {
        super(cause);
    }
}
