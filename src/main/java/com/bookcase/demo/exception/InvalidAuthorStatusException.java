package com.bookcase.demo.exception;

public class InvalidAuthorStatusException extends RuntimeException {

    private static final String ERROR_MESSAGE = "You cannot bring author back from the dead";

    public InvalidAuthorStatusException() {
        super(ERROR_MESSAGE);
    }
}
