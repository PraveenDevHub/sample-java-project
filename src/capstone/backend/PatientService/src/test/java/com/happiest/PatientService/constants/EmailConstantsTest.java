package com.happiest.PatientService.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class EmailConstantsTest {

    @Test
    void testEmailConstants() {
        assertEquals("Appointment Canceled", EmailConstants.CANCELLATION_SUBJECT);
        assertEquals("Appointment Reminder", EmailConstants.REMINDER_SUBJECT);
        assertEquals("New Appointment Booking", EmailConstants.NEW_BOOKING_SUBJECT_DOCTOR);
        assertEquals("Appointment Confirmation", EmailConstants.NEW_BOOKING_SUBJECT_PATIENT);

        assertEquals("Dear Dr. %s,\n\nYour appointment with patient %s on %s at %s has been canceled.\n\nRegards,\nAppointment System", EmailConstants.CANCELLATION_BODY);
        assertEquals("Dear %s,\n\nThis is a reminder for your upcoming appointment with Dr. %s on %s at %s.\nPlease be on time.\n\nRegards,\nAppointment System", EmailConstants.REMINDER_BODY);
        assertEquals("Dear Dr. %s,\n\nYou have a new appointment booking.\n\n Patient Details\n\nPatient Name: %s\nAppointment Date: %s\nAppointment Time: %s\n\nThank you,\nYour Clinic", EmailConstants.BOOKING_BODY_DOCTOR);
        assertEquals("Dear %s,\n\nYour appointment has been successfully booked.\n\nYour appointment details\n\nDoctor Name: Dr. %s\nAppointment Date: %s\nAppointment Time: %s\n\nThank you,\nYour Clinic", EmailConstants.BOOKING_BODY_PATIENT);
    }

    @Test
    void testEmailConstants_Failure() {
        // Simulate incorrect values to ensure the constants are as expected
        assertNotEquals("Appointment Cancelled", EmailConstants.CANCELLATION_SUBJECT);
        assertNotEquals("Reminder for Appointment", EmailConstants.REMINDER_SUBJECT);
        assertNotEquals("Booking Confirmation for Doctor", EmailConstants.NEW_BOOKING_SUBJECT_DOCTOR);
        assertNotEquals("Confirmation of Appointment", EmailConstants.NEW_BOOKING_SUBJECT_PATIENT);

        assertNotEquals("Dear Dr. %s, Your appointment with patient %s on %s at %s has been canceled.", EmailConstants.CANCELLATION_BODY);
        assertNotEquals("Dear %s, This is a reminder for your appointment with Dr. %s on %s at %s.", EmailConstants.REMINDER_BODY);
        assertNotEquals("Dear Dr. %s, You have a new appointment booking. Patient Name: %s, Appointment Date: %s, Appointment Time: %s", EmailConstants.BOOKING_BODY_DOCTOR);
        assertNotEquals("Dear %s, Your appointment has been booked. Doctor Name: Dr. %s, Appointment Date: %s, Appointment Time: %s", EmailConstants.BOOKING_BODY_PATIENT);
    }
}

