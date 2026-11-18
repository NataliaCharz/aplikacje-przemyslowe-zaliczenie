package com.bookcase.demo.advice;

import com.bookcase.demo.exception.AuthorNotFoundException;
import com.bookcase.demo.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthorNotFoundException.class, BookNotFoundException.class})
    public ProblemDetail handle404(RuntimeException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handle403(AccessDeniedException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body("No permission to access this resource");
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handle500(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("A server error occurred: " + ex.getMessage());
//    }
}

