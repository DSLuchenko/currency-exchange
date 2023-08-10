package com.dsluchenko.app.service.exception;

import jakarta.servlet.http.HttpServletResponse;

public class UnavailableExchangeException extends ServiceApplicationException {
    private final static String MESSAGE = "Exchange rate in not available";

    public UnavailableExchangeException() {
        super(MESSAGE, HttpServletResponse.SC_NOT_FOUND);
    }
    public UnavailableExchangeException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }
}
