package com.happiest.PatientService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentAlreadyCanceledException.class)
    public ResponseEntity<String> handleAppointmentAlreadyCanceled(AppointmentAlreadyCanceledException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AppointmentNotScheduledException.class)
    public ResponseEntity<String> handleAppointmentNotScheduled(AppointmentNotScheduledException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<String> handleEmailException(EmailException ex) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<String> handlePatientNotFoundException(PatientNotFoundException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FilterCriteriaException.class)
    public ResponseEntity<String> handleFilterCriteriaException(FilterCriteriaException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<String> handleDoctorNotFoundException(DoctorNotFoundException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppointmentBookingException.class)
    public ResponseEntity<String> handleAppointmentBookingException(AppointmentBookingException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<String> handleAppointmentNotFoundException(AppointmentNotFoundException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
        String errorMessage = String.format("Error occurred at %s: %s", LocalDateTime.now(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
