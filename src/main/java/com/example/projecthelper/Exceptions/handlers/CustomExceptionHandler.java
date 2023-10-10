package com.example.projecthelper.Exceptions.handlers;

import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.util.ResponseResult;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(InvalidFormException.class)
    public ResponseEntity<ResponseResult<Object>> handleInvalidFormException(InvalidFormException ex) {
        ResponseResult<Object> response = ResponseResult.invalidContent(null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

