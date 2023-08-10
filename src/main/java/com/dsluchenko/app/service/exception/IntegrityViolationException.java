package com.dsluchenko.app.service.exception;

import jakarta.servlet.http.HttpServletResponse;

public class IntegrityViolationException extends ServiceApplicationException {
    private static final String MESSAGE = "Data already exists";

    public IntegrityViolationException() {
        super(MESSAGE, HttpServletResponse.SC_CONFLICT);
    }

    public IntegrityViolationException(String message) {
        super(message, HttpServletResponse.SC_CONFLICT);
    }
}

