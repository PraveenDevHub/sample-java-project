package com.happiest.PatientService.constants;

public class EmailConstants {

    public static final String CANCELLATION_SUBJECT = "Appointment Canceled";
    public static final String REMINDER_SUBJECT = "Appointment Reminder";
    public static final String NEW_BOOKING_SUBJECT_DOCTOR = "New Appointment Booking";
    public static final String NEW_BOOKING_SUBJECT_PATIENT = "Appointment Confirmation";

    public static final String CANCELLATION_BODY = "Dear Dr. %s,\n\nYour appointment with patient %s on %s at %s has been canceled.\n\nRegards,\nAppointment System";
    public static final String REMINDER_BODY = "Dear %s,\n\nThis is a reminder for your upcoming appointment with Dr. %s on %s at %s.\nPlease be on time.\n\nRegards,\nAppointment System";
    public static final String BOOKING_BODY_DOCTOR = "Dear Dr. %s,\n\nYou have a new appointment booking.\n\n Patient Details\n\nPatient Name: %s\nAppointment Date: %s\nAppointment Time: %s\n\nThank you,\nYour Clinic";
    public static final String BOOKING_BODY_PATIENT = "Dear %s,\n\nYour appointment has been successfully booked.\n\nYour appointment details\n\nDoctor Name: Dr. %s\nAppointment Date: %s\nAppointment Time: %s\n\nThank you,\nYour Clinic";
}
