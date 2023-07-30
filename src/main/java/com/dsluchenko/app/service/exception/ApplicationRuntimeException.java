package com.dsluchenko.app.service.exception;

public class ApplicationRuntimeException extends RuntimeException {
    private static final String message = "Internal server error";

    public ApplicationRuntimeException() {
        super(message);
    }

    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRuntimeException(Throwable cause) {
        super(cause);
    }
}
