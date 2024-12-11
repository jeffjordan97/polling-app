package com.dizplai.polling.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Ensures exceptions are handled in a standardized way.
    // E.g. PollService detects an invalid poll, throws PollInvalidException,
    // Exception is caught by GlobalExceptionHandler to send a user-friendly error response to the client
    @ExceptionHandler(PollInvalidException.class)
    public ResponseEntity<String> handlePollInvalidException(PollInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
