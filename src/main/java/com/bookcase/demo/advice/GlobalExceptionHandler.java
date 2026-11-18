package com.bookcase.demo.advice;

import com.bookcase.demo.exception.AuthorNotFoundException;
import com.bookcase.demo.exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.LinkedHashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthorNotFoundException.class, BookNotFoundException.class})
    public ProblemDetail handle404(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handle400Validation(MethodArgumentNotValidException ex) {
        Set<String> invalidList = new LinkedHashSet<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(f -> invalidList.add(f.getField() + ": " + f.getDefaultMessage()));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setProperty("errors", invalidList);
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handle400Enum(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        if (ex.getCause() instanceof InvalidFormatException e && !e.getPath().isEmpty()){
            String field = e.getPath().get(0).getPropertyName();
            problemDetail.setProperty("errors", Set.of(field));
        }
        return problemDetail;
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

