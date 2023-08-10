package com.dsluchenko.app.service.exception;

import jakarta.servlet.http.HttpServletResponse;

public class ExchangeRateNotFoundException extends ServiceApplicationException {
    private static final String MESSAGE = "Rate with codes not found";

    public ExchangeRateNotFoundException() {

        super(MESSAGE, HttpServletResponse.SC_NOT_FOUND);
    }

    public ExchangeRateNotFoundException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }

}
