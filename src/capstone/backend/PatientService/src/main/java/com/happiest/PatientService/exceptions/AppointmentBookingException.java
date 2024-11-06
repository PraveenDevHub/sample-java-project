package com.happiest.PatientService.exceptions;

public class AppointmentBookingException extends RuntimeException {
    public AppointmentBookingException(String message) {
        super(message);
    }

    public AppointmentBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
