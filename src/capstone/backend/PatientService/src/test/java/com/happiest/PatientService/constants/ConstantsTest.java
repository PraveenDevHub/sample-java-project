package com.happiest.PatientService.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConstantsTest {

    @Test
    void testConstants() {
        assertEquals("Fetching all appointments", Constants.FETCHING_ALL_APPOINTMENTS);
        assertEquals("Fetching upcoming appointments for patient ID: {}", Constants.FETCHING_UPCOMING_APPOINTMENTS);
        assertEquals("No upcoming appointments found for patient ID: {}", Constants.NO_UPCOMING_APPOINTMENTS_FOUND);
        assertEquals("Fetching appointment history for patient ID: {}", Constants.FETCHING_APPOINTMENT_HISTORY);
        assertEquals("No appointment history found for patient ID: {}", Constants.NO_APPOINTMENT_HISTORY_FOUND);
        assertEquals("Attempting to cancel appointment ID: {} for patient ID: {}", Constants.ATTEMPT_CANCEL_APPOINTMENT);
        assertEquals("Appointment not found for ID: ", Constants.APPOINTMENT_NOT_FOUND);
        assertEquals("Patient is not authorized to cancel this appointment.", Constants.UNAUTHORIZED_PATIENT_ACCESS);
        assertEquals("This appointment has already been canceled.", Constants.APPOINTMENT_ALREADY_CANCELED);
        assertEquals("Appointment ID: {} canceled successfully", Constants.APPOINTMENT_CANCEL_SUCCESS);
        assertEquals("Sending reminders for appointments between {} and {}", Constants.SENDING_REMINDER);
        assertEquals("Reminder email sent for appointment ID: {}", Constants.REMINDER_EMAIL_SENT);
        assertEquals("Fetching available dates for doctor ID: {}", Constants.FETCH_AVAILABLE_DATES);
        assertEquals("All available slots greater than current time: {}", Constants.AVAILABLE_SLOTS_MESSAGE);
        assertEquals("Booking appointment for patient ID: {} and doctor ID: {}", Constants.BOOKING_APPOINTMENT);
        assertEquals("Appointment booked successfully for patient ID: {} and doctor ID: {}", Constants.APPOINTMENT_BOOKED_SUCCESS);
        assertEquals("Doctor not found for ID: ", Constants.DOCTOR_NOT_FOUND);
        assertEquals("Patient not found for ID: ", Constants.PATIENT_NOT_FOUND);
        assertEquals("Fetched all doctors: {}", Constants.FETCHED_ALL_DOCTORS);
        assertEquals("Filtered approved doctors: {}", Constants.FETCHED_APPROVED_DOCTORS);
        assertEquals("Approved", Constants.APPROVED);
        assertEquals("Filtered doctors: {}", Constants.FILTERED_DOCTORS);
        assertEquals("Fetching patient with ID: {}", Constants.FETCHING_PATIENT);
        assertEquals("Updating profile for patient ID: {}", Constants.UPDATING_PATIENT_PROFILE);
        assertEquals("Updated user details for patient ID: {}", Constants.UPDATED_USER_DETAILS);
        assertEquals("Updated patient profile for patient ID: {}", Constants.UPDATED_PATIENT_PROFILE);
        assertEquals("Successfully retrieved", Constants.SUCCESSFUL_RETRIEVAL);
        assertEquals("Successfully canceled appointment", Constants.SUCCESSFUL_CANCELLATION);
        assertEquals("Failed to book the appointment", Constants.FAILED_BOOKING);
        assertEquals("Invalid date format", Constants.INVALID_DATE_FORMAT);
        assertEquals("Internal server error", Constants.INTERNAL_SERVER_ERROR);
        assertEquals("Successfully retrieved appointment history for patient with ID: {}", Constants.SUCCESSFUL_APPOINTMENT_HISTORY_RETRIEVAL);
        assertEquals("Attempting to cancel appointment with ID: {} for patient with ID: {}", Constants.CANCELING_APPOINTMENT);
        assertEquals("Successfully canceled appointment with ID: {} for patient with ID: {}", Constants.SUCCESSFUL_APPOINTMENT_CANCELLATION);
        assertEquals("Appointment or patient not found: appointmentId = {}, patientId = {}", Constants.APPOINTMENT_OR_PATIENT_NOT_FOUND);
        assertEquals("Fetching upcoming appointments for patient with ID: {}", Constants.LOG_FETCH_UPCOMING_APPOINTMENTS);
        assertEquals("Successfully retrieved upcoming appointments for patient with ID: {}", Constants.LOG_UPCOMING_APPOINTMENTS_SUCCESS);
        assertEquals("Patient with ID {} not found", Constants.LOG_PATIENT_NOT_FOUND);
        assertEquals("Available dates retrieved successfully", Constants.AVAILABLE_DATES_RETRIEVED);
        assertEquals("Available slots retrieved successfully", Constants.AVAILABLE_SLOTS_RETRIEVED);
        assertEquals("Appointment booked successfully", Constants.APPOINTMENT_BOOKED_SUCCESSFULLY);
        assertEquals("Failed to book the appointment due to client error", Constants.FAILED_TO_BOOK_APPOINTMENT);
        assertEquals("Fetching available dates for doctor with ID: {}", Constants.LOG_FETCH_AVAILABLE_DATES);
        assertEquals("Successfully retrieved available dates for doctor with ID: {}", Constants.LOG_AVAILABLE_DATES_SUCCESS);
        assertEquals("Doctor with ID {} not found", Constants.LOG_DOCTOR_NOT_FOUND);
        assertEquals("Fetching available slots for doctor with ID: {} on date: {}", Constants.LOG_FETCH_AVAILABLE_SLOTS);
        assertEquals("Successfully retrieved available slots for doctor with ID: {} on date: {}", Constants.LOG_AVAILABLE_SLOTS_SUCCESS);
        assertEquals("Invalid date format for date: {}", Constants.LOG_INVALID_DATE_FORMAT);
        assertEquals("Attempting to book an appointment for patient ID: {}", Constants.LOG_BOOK_APPOINTMENT);
        assertEquals("Appointment booked successfully for patient ID: {}", Constants.LOG_APPOINTMENT_BOOKED_SUCCESS);
        assertEquals("Failed to book the appointment: {}", Constants.LOG_BOOKING_FAILED);
        assertEquals("Error booking appointment: {}", Constants.LOG_BOOKING_ERROR);
        assertEquals("Doctors retrieved successfully", Constants.DOCTORS_RETRIEVED);
        assertEquals("Filtered doctors retrieved successfully", Constants.DOCTORS_FILTERED_RETRIEVED);
        assertEquals("Patient profile retrieved successfully", Constants.PATIENT_PROFILE_RETRIEVED);
        assertEquals("Patient profile updated successfully", Constants.PATIENT_PROFILE_UPDATED);
        assertEquals("Fetching doctors with limit: {}", Constants.LOG_FETCH_DOCTORS);
        assertEquals("Filtering doctors with parameters - state: {}, city: {}, specialization: {}, hospital: {}, searchTerm: {}", Constants.LOG_FILTER_DOCTORS);
        assertEquals("Fetching profile for patient ID: {}", Constants.LOG_GET_PATIENT_PROFILE);
        assertEquals("Updating profile for patient ID: {}", Constants.LOG_UPDATE_PATIENT_PROFILE);
    }
}

