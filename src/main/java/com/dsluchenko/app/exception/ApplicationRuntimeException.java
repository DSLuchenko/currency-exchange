package com.dsluchenko.app.exception;

import jakarta.servlet.http.HttpServletResponse;

public class ApplicationRuntimeException extends RuntimeException {
    private static final String MESSAGE = "Internal server error";
    private static final int  HTTP_STATUS = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    private final int httpStatus;

    public ApplicationRuntimeException() {
        super(MESSAGE);
        this.httpStatus = HTTP_STATUS;
    }

    public ApplicationRuntimeException(String message) {
        super(message);
        this.httpStatus = HTTP_STATUS;
    }

    public ApplicationRuntimeException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
