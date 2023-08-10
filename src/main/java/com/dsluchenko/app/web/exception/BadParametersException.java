package com.dsluchenko.app.web.exception;

import jakarta.servlet.http.HttpServletResponse;

public class BadParametersException extends WebFilterApplicationException {
    private static final String MESSAGE = "Wrong request parameters";
    private static final int HTTP_STATUS = HttpServletResponse.SC_BAD_REQUEST;

    public BadParametersException() {
        super(MESSAGE, HTTP_STATUS);
    }

    public BadParametersException(String message) {
        super(message, HTTP_STATUS);
    }

}
