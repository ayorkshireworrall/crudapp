package com.alex.worrall.crudapp.framework.Exceptions;

public class WrongActionClaimException extends RuntimeException {
    public WrongActionClaimException(String message) {
        super(message);
    }
}
