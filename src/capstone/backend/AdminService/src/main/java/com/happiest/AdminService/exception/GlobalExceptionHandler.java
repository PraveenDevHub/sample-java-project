package com.happiest.AdminService.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);


//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        logger.error("ResourceNotFoundException: {}", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ex.getMessage());    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("ResourceNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());    }


    // Handle invalid operations
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOperationException(InvalidOperationException ex) {
        logger.error("InvalidOperationException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handle email sending failure
    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<String> handleEmailSendException(EmailSendException ex) {
        logger.error("EmailSendException: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
//        Map<String, String> response = new HashMap<>();
//        response.put("error", "Internal Server Error");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }

}


