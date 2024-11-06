package com.happiest.APIGatewayJWT2.constants;

public class LoggerConstants {
    // User Registration
    public static final String REGISTERING_USER = "Registering user with email: {}";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully: {}";

    // User Login
    public static final String LOGIN_ATTEMPT = "Login attempt for user: {}";
    public static final String LOGIN_SUCCESSFUL = "Login successful for user: {}";

    // Password Reset
    public static final String PASSWORD_RESET_REQUESTED = "Password reset requested for email: {}";
    public static final String RESETTING_PASSWORD = "Resetting password for token: {}";
    public static final String PASSWORD_RESET_SUCCESSFUL = "Password reset successful for token: {}";

    // Verification
    public static final String CHECKING_VERIFICATION_STATUS = "Checking verification status for email: {}";
    public static final String USER_NOT_FOUND = "User not found: {}";
    public static final String VERIFYING_USER = "Verifying user with token: {}";
    public static final String USER_VERIFIED_SUCCESSFULLY = "User verified successfully for token: {}";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token: {}";
    public static final String ERROR_REDIRECTING_TO_LOGIN = "Error redirecting to login: {}";

    // Email
    public static final String SENDING_TEST_EMAIL = "Sending test email";
    public static final String RECEIVED_REQUEST_TO_SEND_VERIFICATION_EMAIL = "Received request to send verification email for: {}";
    public static final String VERIFICATION_EMAIL_SENT = "Verification email sent for: {}";

    // Schedule Update
    public static final String UPDATING_SCHEDULE = "Updating schedule for doctor: {}";
    public static final String SCHEDULE_CONFLICT = "Schedule conflict for doctor: {}";
    public static final String SCHEDULE_UPDATED_SUCCESSFULLY = "Schedule updated successfully for doctor: {}";
    // Default Schedule
    public static final String SAVING_DEFAULT_SCHEDULE = "Saving default schedule";
    public static final String DEFAULT_SCHEDULE_SAVED_SUCCESSFULLY = "Default schedule saved successfully";
    public static final String FETCHING_DEFAULT_SCHEDULE = "Fetching default schedule for doctorId: {}, dayOfWeek: {}";

    // Doctor Profiles
    public static final String FETCHING_ALL_DOCTOR_PROFILES = "Fetching all doctor profiles";
    // Appointments
    public static final String CREATING_APPOINTMENT = "Creating appointment for patient: {}";
    public static final String FETCHING_UPCOMING_APPOINTMENTS = "Fetching upcoming appointments for doctorId: {}";
    public static final String CANCELLING_APPOINTMENT = "Cancelling appointmentId: {}";
    public static final String COMPLETING_APPOINTMENT = "Completing appointmentId: {}";
    public static final String FETCHING_DOCTOR_APPOINTMENT_HISTORY = "Fetching appointment history for doctorId: {}";
    public static final String CANCELLING_ALL_APPOINTMENTS = "Cancelling all appointments for doctorId: {}";
    public static final String FETCHING_APPOINTMENT_HISTORY = "Fetching appointment history for patientId: {}";
    public static final String CANCELLING_APPOINTMENT_FOR_PATIENT = "Cancelling appointmentId: {} for patientId: {}";
    public static final String FETCHING_UPCOMING_APPOINTMENTS_FOR_PATIENT = "Fetching upcoming appointments for patientId: {}";

    // Admin
    public static final String FETCHING_DASHBOARD_COUNTS = "Fetching dashboard counts for admin";
    public static final String FETCHING_APPOINTMENT_STATISTICS = "Fetching appointment statistics for admin";
    public static final String FETCHING_PENDING_DOCTOR_APPROVALS = "Fetching pending doctor approvals";
    public static final String APPROVING_DOCTOR = "Approving doctorId: {}";
    public static final String REJECTING_DOCTOR = "Rejecting doctorId: {}";

    // Fetching Doctors and Patients
    public static final String FETCHING_ALL_DOCTORS = "Fetching all doctors with approvalStatus: {}";
    public static final String FETCHING_ALL_PATIENTS = "Fetching all patients";

    // Fetching Profiles
    public static final String FETCHING_DOCTOR_PROFILE = "Fetching doctor profile for doctorId: {}";
    public static final String FETCHING_USER_DETAILS = "Fetching user details for userId: {}";

    // Patient Controller
    public static final String FETCHING_DOCTORS = "Fetching doctors with limit: {}";
    public static final String FILTERING_DOCTORS = "Filtering doctors with state: {}, city: {}, specialization: {}, hospital: {}, searchTerm: {}";
    public static final String FETCHING_PATIENT_BY_ID = "Fetching patient by ID: {}";
    public static final String FETCHING_APPOINTMENTS_BY_PATIENT_ID = "Fetching appointments for patient ID: {}";
    public static final String FETCHING_AVAILABLE_DATES = "Fetching available dates for doctor ID: {}";
    public static final String FETCHING_AVAILABLE_SLOTS = "Fetching available slots for doctor ID: {}, date: {}";
    public static final String BOOKING_APPOINTMENT = "Booking appointment for patient: {}";
    public static final String FETCHING_PATIENT_PROFILE = "Fetching patient profile for patient ID: {}";
    public static final String UPDATING_PATIENT_PROFILE = "Updating patient profile for patient ID: {}";
}
