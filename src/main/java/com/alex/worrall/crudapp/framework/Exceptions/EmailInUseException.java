package com.alex.worrall.crudapp.framework.Exceptions;

public class EmailInUseException extends InUseException {
    public EmailInUseException(String message) {
        super(message);
    }
}
