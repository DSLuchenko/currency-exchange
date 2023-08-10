package com.dsluchenko.app.service.exception;

import jakarta.servlet.http.HttpServletResponse;

public class CurrencyNotFoundException extends ServiceApplicationException {
    private static final String MESSAGE = "Currency not found";

    public CurrencyNotFoundException() {
        super(MESSAGE, HttpServletResponse.SC_NOT_FOUND);
    }

    public CurrencyNotFoundException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }

}
