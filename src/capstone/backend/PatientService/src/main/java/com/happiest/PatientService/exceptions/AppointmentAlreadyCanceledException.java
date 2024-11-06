package com.happiest.PatientService.exceptions;

public class AppointmentAlreadyCanceledException extends RuntimeException {
    public AppointmentAlreadyCanceledException(String message) {
        super(message);
    }
}
