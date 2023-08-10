package com.dsluchenko.app.web.exception;

import com.dsluchenko.app.exception.ApplicationRuntimeException;

public class WebFilterApplicationException extends ApplicationRuntimeException {
    public WebFilterApplicationException() {
        super();
    }

    public WebFilterApplicationException(String message) {
        super(message);
    }

    public WebFilterApplicationException(String message, int httpStatus) {
        super(message, httpStatus);
    }
}
