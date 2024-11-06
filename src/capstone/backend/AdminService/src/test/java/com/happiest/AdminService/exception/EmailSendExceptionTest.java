package com.happiest.AdminService.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailSendExceptionTest {
    @Test
    public void testEmailSendException() {
        String message = "Email sending failed";
        EmailSendException exception = new EmailSendException(message);
        assertEquals(message, exception.getMessage());
    }

}
