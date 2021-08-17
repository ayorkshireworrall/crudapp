package com.alex.worrall.crudapp.framework.Exceptions;

public class UsernameInUseException extends RuntimeException {
    public UsernameInUseException(String message) {
        super(message);
    }
}
