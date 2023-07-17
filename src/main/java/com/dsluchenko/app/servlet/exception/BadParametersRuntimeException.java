package com.dsluchenko.app.servlet.exception;

public class BadParametersRuntimeException extends RuntimeException {
    private static final String message = "Wrong request parameters";

    public BadParametersRuntimeException() {
        super(message);
    }

    public BadParametersRuntimeException(String message) {
        super(message);
    }

    public BadParametersRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadParametersRuntimeException(Throwable cause) {
        super(cause);
    }
}
