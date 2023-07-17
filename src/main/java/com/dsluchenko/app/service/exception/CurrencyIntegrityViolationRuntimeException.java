package com.dsluchenko.app.service.exception;

public class CurrencyIntegrityViolationRuntimeException extends RuntimeException {
    private static final String message = "Currency already exists";

    public CurrencyIntegrityViolationRuntimeException() {
        super(message);
    }

    public CurrencyIntegrityViolationRuntimeException(String message) {
        super(message);
    }

    public CurrencyIntegrityViolationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyIntegrityViolationRuntimeException(Throwable cause) {
        super(cause);
    }
}
