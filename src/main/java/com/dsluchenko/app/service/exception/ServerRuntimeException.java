package com.dsluchenko.app.service.exception;

public class ServerRuntimeException extends RuntimeException {
    private static final String message = "Internal server error";

    public ServerRuntimeException() {
        super(message);
    }

    public ServerRuntimeException(String message) {
        super(message);
    }

    public ServerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerRuntimeException(Throwable cause) {
        super(cause);
    }
}
