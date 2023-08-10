package com.dsluchenko.app.data.dao.exception;

import jakarta.servlet.http.HttpServletResponse;

public class ConstraintViolationException extends DaoApplicationException {
    private static final String MESSAGE = "Constraint has been violated";
    private static final int HTTP_STATUS = HttpServletResponse.SC_CONFLICT;

    public ConstraintViolationException() {
        super(MESSAGE, HTTP_STATUS);
    }
}
