package com.artax.bil.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
// log, map to proper error object
        return ResponseEntity.status(500).body("Internal Server Error: " + ex.getMessage());
    }
}