package com.example.projecthelper.Exceptions;

public class InvalidFormException extends RuntimeException {
    public static final String ABSENT = "ABSENT";
    public static final String WRONG = "WRONG";

    private String invalidKey;
    private String invalidType;


    public InvalidFormException() {
        super();
    }

    public InvalidFormException(String message) {
        super(message);
    }

    public InvalidFormException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFormException(Throwable cause) {
        super(cause);
    }
}

