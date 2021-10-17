package com.alex.worrall.crudapp.framework.Exceptions;

public class InUseException extends RuntimeException {
    public InUseException(String message) {
        super(message);
    }
}
