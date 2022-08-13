package com.nta.teabreakorder.exception;

import com.nta.teabreakorder.common.ConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ConfigHelper configReader;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), ex.getMessage());
        log.error("{} : {}", ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<?> SQLConstrainsException(DataIntegrityViolationException ex, WebRequest request) {
        String errorMessage = ex.getMostSpecificCause().getMessage();
        String key = errorMessage.split(" key ")[1].replaceAll("\'", "");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), configReader.getValue(key, "Error"), request.getDescription(false), ex.getMessage());
        log.error("{} : {}", ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, ApiException.class})
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), ex.getMessage());
        log.error("{} : {}", ex.getClass(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
