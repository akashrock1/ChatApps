package com.example.ChatApp.advices;


import com.example.ChatApp.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> ResourceNotFoundHandler(ResourceNotFoundException ex){
        return new ResponseEntity<>(new ApiError(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> dataIntegrityHandler(UsernameNotFoundException ex){
        return new ResponseEntity<>(new ApiError("Email is not correct"), HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> dataIntegrityHandler(Exception ex){
//        return new ResponseEntity<>(new ApiError(ex.getLocalizedMessage()), HttpStatus.CONFLICT);
//    }



}
