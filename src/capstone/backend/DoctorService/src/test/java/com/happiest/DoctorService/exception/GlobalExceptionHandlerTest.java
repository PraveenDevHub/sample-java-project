package com.happiest.DoctorService.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.happiest.DoctorService.exception.EmailException;
import com.happiest.DoctorService.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<String> response = globalExceptionHandler.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", extractMessage(response.getBody()));
    }

    @Test
    void testHandleDoctorNotFoundException() {
        DoctorNotFoundException ex = new DoctorNotFoundException("Doctor not found");
        ResponseEntity<String> response = globalExceptionHandler.handleDoctorNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Doctor not found", extractMessage(response.getBody()));
    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<String> response = globalExceptionHandler.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", extractMessage(response.getBody()));
    }

    @Test
    void testHandleAppointmentAlreadyCancelledException() {
        AppointmentAlreadyCancelledException ex = new AppointmentAlreadyCancelledException("Appointment already cancelled");
        ResponseEntity<String> response = globalExceptionHandler.handleAppointmentAlreadyCancelledException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Appointment already cancelled", extractMessage(response.getBody()));
    }

    @Test
    void testHandleValidationException() {
        ValidationException ex = new ValidationException("Validation error");
        ResponseEntity<String> response = globalExceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", extractMessage(response.getBody()));
    }

    @Test
    void testHandleDefaultScheduleException() {
        DefaultScheduleException ex = new DefaultScheduleException("Default schedule error");
        ResponseEntity<String> response = globalExceptionHandler.handleDefaultScheduleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Default schedule error", extractMessage(response.getBody()));
    }

    @Test
    void testHandleDoctorProfileException() {
        DoctorProfileException ex = new DoctorProfileException("Doctor profile error");
        ResponseEntity<String> response = globalExceptionHandler.handleDoctorProfileException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Doctor profile error", extractMessage(response.getBody()));
    }

    @Test
    void testHandleDuplicateScheduleException() {
        DuplicateScheduleException ex = new DuplicateScheduleException("Duplicate schedule");
        ResponseEntity<String> response = globalExceptionHandler.handleDuplicateScheduleException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate schedule", extractMessage(response.getBody()));
    }

    @Test
    void testHandleFileStorageException() {
        FileStorageException ex = new FileStorageException("File storage error");
        ResponseEntity<String> response = globalExceptionHandler.handleFileStorageException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("File storage error", extractMessage(response.getBody()));
    }

    @Test
    void testHandleMyFileNotFoundException() {
        MyFileNotFoundException ex = new MyFileNotFoundException("File not found");
        ResponseEntity<String> response = globalExceptionHandler.handleMyFileNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found", extractMessage(response.getBody()));
    }

    @Test
    void testHandleEmailException() {
        EmailException ex = new EmailException("Email error");
        ResponseEntity<String> response = globalExceptionHandler.handleEmailException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Email error", extractMessage(response.getBody()));
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new Exception("Global error");
        ResponseEntity<String> response = globalExceptionHandler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Global error", extractMessage(response.getBody()));
    }

    private String extractMessage(String responseBody) {
        return responseBody.substring(responseBody.indexOf(": ") + 2);
    }
}
