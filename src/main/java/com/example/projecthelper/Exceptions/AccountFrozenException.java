package com.example.projecthelper.Exceptions;

public class AccountFrozenException extends RuntimeException {

    public AccountFrozenException() {
        super();
    }

    public AccountFrozenException(String message) {
        super(message);
    }

    public AccountFrozenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountFrozenException(Throwable cause) {
        super(cause);
    }
}
