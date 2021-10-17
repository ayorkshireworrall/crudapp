package com.alex.worrall.crudapp.framework.Exceptions;

public class UsernameInUseException extends InUseException {
    public UsernameInUseException(String message) {
        super(message);
    }
}
