package com.happiest.APIGatewayJWT2.test;

import com.happiest.APIGatewayJWT2.exception.*;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTests {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException("Email already exists");
        ResponseEntity<?> response = globalExceptionHandler.handleEmailAlreadyExistsException(exception, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    public void testHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        ResponseEntity<?> response = globalExceptionHandler.handleUserNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    public void testHandleInvalidTokenException() {
        InvalidTokenException exception = new InvalidTokenException("Invalid token");
        ResponseEntity<?> response = globalExceptionHandler.handleInvalidTokenException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    public void testHandleEmailNotFoundException() {
        EmailNotFoundException exception = new EmailNotFoundException("Email not found");
        ResponseEntity<String> response = globalExceptionHandler.handleEmailNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Email not found", response.getBody());
    }

    @Test
    public void testHandleEmailNotVerifiedException() {
        EmailNotVerifiedException exception = new EmailNotVerifiedException("Email not verified");
        ResponseEntity<String> response = globalExceptionHandler.handleEmailNotVerifiedException(exception, webRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Email not verified", response.getBody());
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal argument");
        ResponseEntity<String> response = globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Illegal argument", response.getBody());
    }

    @Test
    public void testHandleEmailSendingException() {
        EmailSendingException exception = new EmailSendingException("Email sending failed", new RuntimeException("Cause"));
        ResponseEntity<String> response = globalExceptionHandler.handleEmailSendingException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error: Email sending failed", response.getBody());
    }

    @Test
    public void testHandleTokenException() {
        TokenException exception = new TokenException("Token error");
        ResponseEntity<String> response = globalExceptionHandler.handleTokenException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token error: Token error", response.getBody());
    }

    @Test
    public void testHandleUsernameNotFoundException() {
        UsernameNotFoundException exception = new UsernameNotFoundException("Username not found");
        ResponseEntity<String> response = globalExceptionHandler.handleUsernameNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Username not found", response.getBody());
    }

    @Test
    public void testHandleGlobalException() {
        Exception exception = new Exception("Global error");
        ResponseEntity<?> response = globalExceptionHandler.handleGlobalException(exception, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Global error", response.getBody());
    }

    @Test
    public void testHandleFeignException() {
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(400);
        when(feignException.contentUTF8()).thenReturn("Feign error");

        ResponseEntity<String> response = globalExceptionHandler.handleFeignException(feignException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Feign error", response.getBody());
    }
}
