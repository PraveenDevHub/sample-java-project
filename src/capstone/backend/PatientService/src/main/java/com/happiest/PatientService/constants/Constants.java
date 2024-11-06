package com.happiest.PatientService.constants;

public class Constants {

    public static final String FETCHING_ALL_APPOINTMENTS = "Fetching all appointments";
    public static final String FETCHING_UPCOMING_APPOINTMENTS = "Fetching upcoming appointments for patient ID: {}";
    public static final String NO_UPCOMING_APPOINTMENTS_FOUND = "No upcoming appointments found for patient ID: {}";
    public static final String FETCHING_APPOINTMENT_HISTORY = "Fetching appointment history for patient ID: {}";
    public static final String NO_APPOINTMENT_HISTORY_FOUND = "No appointment history found for patient ID: {}";
    public static final String ATTEMPT_CANCEL_APPOINTMENT = "Attempting to cancel appointment ID: {} for patient ID: {}";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found for ID: ";
    public static final String UNAUTHORIZED_PATIENT_ACCESS = "Patient is not authorized to cancel this appointment.";
    public static final String APPOINTMENT_ALREADY_CANCELED = "This appointment has already been canceled.";
    public static final String APPOINTMENT_CANCEL_SUCCESS = "Appointment ID: {} canceled successfully";
    public static final String SENDING_REMINDER = "Sending reminders for appointments between {} and {}";
    public static final String REMINDER_EMAIL_SENT = "Reminder email sent for appointment ID: {}";
    public static final String FETCH_AVAILABLE_DATES = "Fetching available dates for doctor ID: {}";
    public static final String AVAILABLE_SLOTS_MESSAGE = "All available slots greater than current time: {}";
    public static final String BOOKING_APPOINTMENT = "Booking appointment for patient ID: {} and doctor ID: {}";
    public static final String APPOINTMENT_BOOKED_SUCCESS = "Appointment booked successfully for patient ID: {} and doctor ID: {}";
    public static final String DOCTOR_NOT_FOUND = "Doctor not found for ID: ";
    public static final String PATIENT_NOT_FOUND = "Patient not found for ID: ";
    public static final String FETCHED_ALL_DOCTORS = "Fetched all doctors: {}";
    public static final String FETCHED_APPROVED_DOCTORS = "Filtered approved doctors: {}";
    public static final String APPROVED = "Approved";
    public static final String FILTERED_DOCTORS = "Filtered doctors: {}";
    public static final String FETCHING_PATIENT = "Fetching patient with ID: {}";
    public static final String UPDATING_PATIENT_PROFILE = "Updating profile for patient ID: {}";
    public static final String UPDATED_USER_DETAILS = "Updated user details for patient ID: {}";
    public static final String UPDATED_PATIENT_PROFILE = "Updated patient profile for patient ID: {}";
    public static final String SUCCESSFUL_RETRIEVAL = "Successfully retrieved";
    public static final String SUCCESSFUL_CANCELLATION = "Successfully canceled appointment";
    public static final String FAILED_BOOKING = "Failed to book the appointment";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    public static final String SUCCESSFUL_APPOINTMENT_HISTORY_RETRIEVAL = "Successfully retrieved appointment history for patient with ID: {}";
    public static final String CANCELING_APPOINTMENT = "Attempting to cancel appointment with ID: {} for patient with ID: {}";
    public static final String SUCCESSFUL_APPOINTMENT_CANCELLATION = "Successfully canceled appointment with ID: {} for patient with ID: {}";
    public static final String APPOINTMENT_OR_PATIENT_NOT_FOUND = "Appointment or patient not found: appointmentId = {}, patientId = {}";
    public static final String LOG_FETCH_UPCOMING_APPOINTMENTS = "Fetching upcoming appointments for patient with ID: {}";
    public static final String LOG_UPCOMING_APPOINTMENTS_SUCCESS = "Successfully retrieved upcoming appointments for patient with ID: {}";
    public static final String LOG_PATIENT_NOT_FOUND = "Patient with ID {} not found";
    public static final String AVAILABLE_DATES_RETRIEVED = "Available dates retrieved successfully";
    public static final String AVAILABLE_SLOTS_RETRIEVED = "Available slots retrieved successfully";
    public static final String APPOINTMENT_BOOKED_SUCCESSFULLY = "Appointment booked successfully";
    public static final String FAILED_TO_BOOK_APPOINTMENT = "Failed to book the appointment due to client error";
    public static final String LOG_FETCH_AVAILABLE_DATES = "Fetching available dates for doctor with ID: {}";
    public static final String LOG_AVAILABLE_DATES_SUCCESS = "Successfully retrieved available dates for doctor with ID: {}";
    public static final String LOG_DOCTOR_NOT_FOUND = "Doctor with ID {} not found";
    public static final String LOG_FETCH_AVAILABLE_SLOTS = "Fetching available slots for doctor with ID: {} on date: {}";
    public static final String LOG_AVAILABLE_SLOTS_SUCCESS = "Successfully retrieved available slots for doctor with ID: {} on date: {}";
    public static final String LOG_INVALID_DATE_FORMAT = "Invalid date format for date: {}";
    public static final String LOG_BOOK_APPOINTMENT = "Attempting to book an appointment for patient ID: {}";
    public static final String LOG_APPOINTMENT_BOOKED_SUCCESS = "Appointment booked successfully for patient ID: {}";
    public static final String LOG_BOOKING_FAILED = "Failed to book the appointment: {}";
    public static final String LOG_BOOKING_ERROR = "Error booking appointment: {}";
    public static final String DOCTORS_RETRIEVED = "Doctors retrieved successfully";
    public static final String DOCTORS_FILTERED_RETRIEVED = "Filtered doctors retrieved successfully";
    public static final String PATIENT_PROFILE_RETRIEVED = "Patient profile retrieved successfully";
    public static final String PATIENT_PROFILE_UPDATED = "Patient profile updated successfully";

    public static final String LOG_FETCH_DOCTORS = "Fetching doctors with limit: {}";
    public static final String LOG_FILTER_DOCTORS = "Filtering doctors with parameters - state: {}, city: {}, specialization: {}, hospital: {}, searchTerm: {}";
    public static final String LOG_GET_PATIENT_PROFILE = "Fetching profile for patient ID: {}";
    public static final String LOG_UPDATE_PATIENT_PROFILE = "Updating profile for patient ID: {}";

    public static final String NO_AVAILABLE_SLOTS ="No slots available for selected date " ;
    public static final String NO_AVAILABLE_DATES = "No Dates available for selected doctor";
    public static final String APPOINTMENT_BOOKING_FAILED = "Appointment booking failed";
}
