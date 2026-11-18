package com.bookcase.demo.exception;

public class BookNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Book not found with id:";

    public BookNotFoundException(Long id) {
        super(ERROR_MESSAGE + id);
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
