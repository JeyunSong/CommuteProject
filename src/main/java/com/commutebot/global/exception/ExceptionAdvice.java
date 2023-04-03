package com.commutebot.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    public Object customException(CustomException e) {
        String errorMessage = e.getMessage();
        HttpStatus httpStatus = e.getHttpStatus();
        return new ResponseEntity<>(errorMessage, httpStatus);
    }

//    @ExceptionHandler(Exception.class)
//    public String exception(Exception e) {
//        return e.getMessage();
//    }
}

