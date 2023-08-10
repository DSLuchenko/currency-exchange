package com.dsluchenko.app.service.exception;

import com.dsluchenko.app.exception.ApplicationRuntimeException;

public class ServiceApplicationException extends ApplicationRuntimeException {
    public ServiceApplicationException() {
        super();
    }

    public ServiceApplicationException(String message) {
        super(message);
    }

    public ServiceApplicationException(String message, int httpStatus) {
        super(message, httpStatus);
    }
}
