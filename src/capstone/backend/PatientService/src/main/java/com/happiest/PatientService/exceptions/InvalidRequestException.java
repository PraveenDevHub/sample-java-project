package com.happiest.PatientService.exceptions;

public class InvalidRequestException extends Throwable {
    public InvalidRequestException(String message) {
        super(message);
    }
}
