package com.happiest.DoctorService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Returns 409 CONFLICT
public class AppointmentAlreadyCancelledException extends RuntimeException {
    public AppointmentAlreadyCancelledException(String message) {
        super(message);
    }
}
