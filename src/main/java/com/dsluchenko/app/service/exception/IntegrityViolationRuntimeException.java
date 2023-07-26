package com.dsluchenko.app.service.exception;

public class IntegrityViolationRuntimeException extends RuntimeException {
    private static final String message = "Data already exists";

    public IntegrityViolationRuntimeException() {
        super(message);
    }

    public IntegrityViolationRuntimeException(String message) {
        super(message);
    }

    public IntegrityViolationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntegrityViolationRuntimeException(Throwable cause) {
        super(cause);
    }
}
