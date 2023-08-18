package ru.effectivemobile.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.concurrent.atomic.AtomicInteger;

@RestControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {
    private final AtomicInteger counter = new AtomicInteger(1);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MsgError> authorizeExceptionHandler(AuthenticationException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<MsgError> inputDataExceptionHandler(InputDataException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<MsgError> nullPointerExceptionHandler(NullPointerException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.BAD_REQUEST);
    }
}