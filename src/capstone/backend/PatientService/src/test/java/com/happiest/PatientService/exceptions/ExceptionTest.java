package com.happiest.PatientService.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ExceptionTest {

    @Test
    public void testAppointmentBookingException() {
        String message = "Booking error";
        AppointmentBookingException exception = assertThrows(AppointmentBookingException.class, () -> {
            throw new AppointmentBookingException(message);
        });
        assertEquals(message, exception.getMessage());
    }


    @Test
    public void testAppointmentNotFoundException() {
        String message = "Appointment not found";
        AppointmentNotFoundException exception = assertThrows(AppointmentNotFoundException.class, () -> {
            throw new AppointmentNotFoundException(message);
        });
        assertEquals(message, exception.getMessage());
    }


    @Test
    public void testInvalidOperationException() {
        String message = "Invalid operation";
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            throw new InvalidOperationException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testInvalidRequestException() {
        String message = "Invalid request";
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            throw new InvalidRequestException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testEmailException() {
        String message = "Email error";
        Throwable cause = new Throwable("Cause of the error");
        EmailException exception = assertThrows(EmailException.class, () -> {
            throw new EmailException(message, cause);
        });
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testDoctorNotFoundException() {
        String message = "Doctor not found";
        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> {
            throw new DoctorNotFoundException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testAppointmentAlreadyCanceledException() {
        String message = "Appointment already canceled";
        AppointmentAlreadyCanceledException exception = assertThrows(AppointmentAlreadyCanceledException.class, () -> {
            throw new AppointmentAlreadyCanceledException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testUnauthorizedAccessException() {
        String message = "Unauthorized access";
        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            throw new UnauthorizedAccessException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testResourceNotFoundException() {
        String message = "Resource not found";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testAppointmentNotScheduledException() {
        String message = "Appointment not scheduled";
        AppointmentNotScheduledException exception = assertThrows(AppointmentNotScheduledException.class, () -> {
            throw new AppointmentNotScheduledException(message);
        });
        assertEquals(message, exception.getMessage());
    }
}

