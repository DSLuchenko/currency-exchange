package com.dsluchenko.app.service.exception;

public class CurrencyNotFoundRuntimeException extends ApplicationRuntimeException {
    private static final String message = "Currency not found";

    public CurrencyNotFoundRuntimeException() {
        super(message);
    }

    public CurrencyNotFoundRuntimeException(String message) {
        super(message);
    }

    public CurrencyNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }
}
