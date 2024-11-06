package com.happiest.PatientService.exceptions;

public class PatientNotFoundException extends Exception {

    public PatientNotFoundException(String message) {
        super(message);
    }
}