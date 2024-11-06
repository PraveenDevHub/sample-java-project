package com.happiest.AdminService.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvalidOperationExceptionTest {
    @Test
    public void testInvalidOperationException() {
        String message = "Invalid operation performed";
        InvalidOperationException exception = new InvalidOperationException(message);
        assertEquals(message, exception.getMessage());
    }
}
