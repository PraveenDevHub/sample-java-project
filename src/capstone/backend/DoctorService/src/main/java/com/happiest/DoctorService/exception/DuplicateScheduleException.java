package com.happiest.DoctorService.exception;

public class DuplicateScheduleException extends RuntimeException {
    public DuplicateScheduleException(String message) {
        super(message);
    }
}
