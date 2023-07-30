package com.dsluchenko.app.data.dao.exception;

public class DaoConstraintViolationRuntimeException extends DaoRuntimeException {
    private static final String message = "Constraint has been violated";

    public DaoConstraintViolationRuntimeException() {
        super(message);
    }

    public DaoConstraintViolationRuntimeException(String message) {
        super(message);
    }

    public DaoConstraintViolationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoConstraintViolationRuntimeException(Throwable cause) {
        super(cause);
    }
}
