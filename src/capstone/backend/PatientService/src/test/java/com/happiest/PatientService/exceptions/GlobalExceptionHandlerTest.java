package com.happiest.PatientService.exceptions;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleAppointmentAlreadyCanceled() {
        AppointmentAlreadyCanceledException ex = new AppointmentAlreadyCanceledException("Appointment already canceled");
        ResponseEntity<String> response = globalExceptionHandler.handleAppointmentAlreadyCanceled(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Appointment already canceled"));
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<String> response = globalExceptionHandler.handleResourceNotFound(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Resource not found"));
    }

    @Test
    void testHandleUnauthorizedAccess() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException("Unauthorized access");
        ResponseEntity<String> response = globalExceptionHandler.handleUnauthorizedAccess(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().contains("Unauthorized access"));
    }

    @Test
    void testHandleAppointmentNotScheduled() {
        AppointmentNotScheduledException ex = new AppointmentNotScheduledException("Appointment not scheduled");
        ResponseEntity<String> response = globalExceptionHandler.handleAppointmentNotScheduled(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Appointment not scheduled"));
    }

    @Test
    void testHandleEmailException() {
        EmailException ex = new EmailException("Email error", new RuntimeException("Cause"));
        ResponseEntity<String> response = globalExceptionHandler.handleEmailException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Email error"));
    }

    @Test
    void testHandlePatientNotFoundException() {
        PatientNotFoundException ex = new PatientNotFoundException("Patient not found");
        ResponseEntity<String> response = globalExceptionHandler.handlePatientNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Patient not found"));
    }

    @Test
    void testHandleFilterCriteriaException() {
        FilterCriteriaException ex = new FilterCriteriaException("Invalid filter criteria");
        ResponseEntity<String> response = globalExceptionHandler.handleFilterCriteriaException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid filter criteria"));
    }

    @Test
    void testHandleDoctorNotFoundException() {
        DoctorNotFoundException ex = new DoctorNotFoundException("Doctor not found");
        ResponseEntity<String> response = globalExceptionHandler.handleDoctorNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Doctor not found"));
    }

    @Test
    void testHandleAppointmentBookingException() {
        AppointmentBookingException ex = new AppointmentBookingException("Appointment booking error");
        ResponseEntity<String> response = globalExceptionHandler.handleAppointmentBookingException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Appointment booking error"));
    }

    @Test
    void testHandleAppointmentNotFoundException() {
        AppointmentNotFoundException ex = new AppointmentNotFoundException("Appointment not found");
        ResponseEntity<String> response = globalExceptionHandler.handleAppointmentNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Appointment not found"));
    }

    @Test
    void testHandleInvalidOperationException() {
        InvalidOperationException ex = new InvalidOperationException("Invalid operation");
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidOperationException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid operation"));
    }

    @Test
    void testHandleInvalidRequestException() {
        InvalidRequestException ex = new InvalidRequestException("Invalid request");
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidRequestException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid request"));
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new Exception("Global error");
        ResponseEntity<String> response = globalExceptionHandler.handleGlobalException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Global error"));
    }
}
