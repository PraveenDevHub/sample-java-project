package com.happiest.AdminService.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext context;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testHandleResourceNotFoundException() {
//        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
//        ResponseEntity<String> response = globalExceptionHandler.handleResourceNotFoundException(ex);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Resource not found", response.getBody());
//    }

    @Test
    public void testHandleInvalidOperationException() {
        InvalidOperationException ex = new InvalidOperationException("Invalid operation");
        ResponseEntity<String> response = globalExceptionHandler.handleInvalidOperationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid operation", response.getBody());
    }

    @Test
    public void testHandleEmailSendException() {
        EmailSendException ex = new EmailSendException("Email sending failed");
        ResponseEntity<String> response = globalExceptionHandler.handleEmailSendException(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Email sending failed", response.getBody());
    }

//    @Test
//    public void testHandleGenericException() {
//        Exception ex = new Exception("Unexpected error");
//        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(ex);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertTrue(response.getBody().contains("An unexpected error occurred:"));
//    }
}

