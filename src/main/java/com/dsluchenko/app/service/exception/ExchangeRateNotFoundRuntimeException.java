package com.dsluchenko.app.service.exception;

public class ExchangeRateNotFoundRuntimeException extends RuntimeException {
    private static final String message = "Rate with codes not found";

    public ExchangeRateNotFoundRuntimeException() {
        super(message);
    }

    public ExchangeRateNotFoundRuntimeException(String message) {
        super(message);
    }

    public ExchangeRateNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeRateNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }
}
