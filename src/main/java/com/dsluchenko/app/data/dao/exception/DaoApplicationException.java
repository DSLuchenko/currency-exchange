package com.dsluchenko.app.data.dao.exception;

import com.dsluchenko.app.exception.ApplicationRuntimeException;

public class DaoApplicationException extends ApplicationRuntimeException {
    public DaoApplicationException() {
        super();
    }

    public DaoApplicationException(String message) {
        super(message);
    }

    public DaoApplicationException(String message, int httpStatus) {
        super(message, httpStatus);
    }
}
