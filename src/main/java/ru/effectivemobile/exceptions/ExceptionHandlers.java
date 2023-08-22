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

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<MsgError> imageNotFoundExceptionHandler(ImageNotFoundException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<MsgError> unsupportedMediaTypeExceptionHandler(UnsupportedMediaTypeException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<MsgError> postNotFoundExceptionHandler(PostNotFoundException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<MsgError> forbiddenExceptionHandler(ForbiddenException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FriendshipException.class)
    public ResponseEntity<MsgError> friendshipExceptionHandler(FriendshipException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<MsgError> nullPointerExceptionHandler(NullPointerException e) {
        return new ResponseEntity<>(new MsgError(e.getMessage(), counter.getAndIncrement()),
                HttpStatus.BAD_REQUEST);
    }

}