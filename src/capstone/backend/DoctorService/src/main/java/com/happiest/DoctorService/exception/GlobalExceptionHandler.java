package com.happiest.DoctorService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<String> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppointmentAlreadyCancelledException.class)
    public ResponseEntity<String> handleAppointmentAlreadyCancelledException(AppointmentAlreadyCancelledException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DefaultScheduleException.class)
    public ResponseEntity<String> handleDefaultScheduleException(DefaultScheduleException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DoctorProfileException.class)
    public ResponseEntity<String> handleDoctorProfileException(DoctorProfileException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateScheduleException.class)
    public ResponseEntity<String> handleDuplicateScheduleException(DuplicateScheduleException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<String> handleFileStorageException(FileStorageException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MyFileNotFoundException.class)
    public ResponseEntity<String> handleMyFileNotFoundException(MyFileNotFoundException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<String> handleEmailException(EmailException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

