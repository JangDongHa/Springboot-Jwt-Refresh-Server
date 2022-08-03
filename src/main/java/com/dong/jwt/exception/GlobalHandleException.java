package com.dong.jwt.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalHandleException {

    @ExceptionHandler(value = Exception.class)
    public String proceedAllException(Exception e){
        return String.format("<h1>%s</h1>", e.getMessage());
    }


}
