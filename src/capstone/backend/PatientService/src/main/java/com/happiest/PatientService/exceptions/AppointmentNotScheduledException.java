package com.happiest.PatientService.exceptions;

public class AppointmentNotScheduledException extends RuntimeException {
    public AppointmentNotScheduledException(String message) {
        super(message);
    }
}
