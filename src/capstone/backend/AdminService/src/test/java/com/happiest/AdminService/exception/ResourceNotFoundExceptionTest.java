package com.happiest.AdminService.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceNotFoundExceptionTest {
    @Test
    public void testResourceNotFoundException() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }
}
