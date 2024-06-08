package com.googleTrendsBigQuery.googleTrendsRestApis.exception;

import com.googleTrendsBigQuery.googleTrendsRestApis.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false)), HttpStatus.BAD_REQUEST);
    }
}