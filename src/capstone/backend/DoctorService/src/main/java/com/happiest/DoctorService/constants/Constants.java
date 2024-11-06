package com.happiest.DoctorService.constants;

public class Constants {

    public static final String DOCTOR_NOT_FOUND = "Doctor not found";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found with ID: ";
    public static final String NO_PAST_APPOINTMENTS = "No past appointments found for doctor with ID: ";
    public static final String APPOINTMENT_ALREADY_CANCELLED = "Appointment with ID %d is already canceled.";
    public static final String CANNOT_COMPLETE_CANCELLED_APPOINTMENT = "Cannot complete a canceled appointment with ID %d";
    public static final String DUPLICATE_SCHEDULE = "Schedule already exists for the selected date and time.";
    public static final String INVALID_DEFAULT_SCHEDULE_FORMAT = "Invalid defaultSchedule format";
    public static final String ERROR_CONVERTING_TIME_SLOTS = "Error converting time slots to JSON for day";
    public static final String INVALID_SCHEDULE_DETAILS = "Invalid schedule details for day: ";
    public static final String FAILED_TO_SAVE_DEFAULT_SCHEDULE = "Failed to save default schedule";
    public static final String LOG_SAVING_DEFAULT_SCHEDULE = "Saving default schedule for doctorId: {}";
    public static final String DOCTOR_NOT_FOUND_WITH_ID = "Doctor with ID %d not found.";
    public static final String DOCTOR_NOT_FOUND_EXCEPTION = "Doctor not found";
    public static final String CANCELLATION_EMAIL_SUBJECT = "Appointment Cancellation Notification";
    public static final String CANCELLATION_EMAIL_BODY_TEMPLATE = "Dear %s,\n\n" +
            "Your appointment has been cancelled. Here are the details:\n%s\n\n" +
            "Please reschedule your appointment at your earliest convenience.\n\n" +
            "Best regards,\nYour Health Care Team";
    public static final String FILE_STORAGE_EXCEPTION = "Could not create the directory where the uploaded files will be stored.";
    public static final String INVALID_PATH_SEQUENCE = "Sorry! Filename contains invalid path sequence ";
    public static final String FILE_STORAGE_ERROR = "Could not store file %s. Please try again!";
    public static final String FILE_NOT_FOUND = "File not found ";
    public static final String DATE_REQUIRED = "Date is required.";
    public static final String APPOINTMENTS_CANCELLED = "All appointments for the selected date have been cancelled.";
    public static final String DEFAULT_SCHEDULE_SAVE_ERROR = "Error saving default schedule";
    public static final String DEFAULT_SCHEDULE_FETCH_ERROR = "Error fetching default schedule";
    public static final String DOCTOR_PROFILES_FETCH_ERROR = "Error fetching doctor profiles";
    public static final String INVALID_TIME_BLOCK = "Start time must be before end time and they cannot be the same.";
    public static final String INVALID_SLOT_DURATION = "Slot duration exceeds the time block duration.";
    public static final String INVALID_PAST_TIME = "Cannot set schedule for past time.";


    public static final String LOG_CREATE_DOCTOR_PROFILE = "Creating doctor profile for doctorId: {}";
    public static final String LOG_DOCTOR_PROFILE_CREATED = "Doctor profile created successfully for doctorId: {}";
    public static final String LOG_DEFAULT_SCHEDULE_SAVED = "Default schedule saved successfully for doctorId: {}";
    public static final String LOG_FETCHING_DEFAULT_SCHEDULE = "Fetching default schedule for doctorId: {} on day: {}";
    public static final String LOG_DEFAULT_SCHEDULE_FETCHED = "Default schedule fetched successfully for doctorId: {} on day: {}";
    public static final String LOG_FETCHING_ALL_PROFILES = "Fetching all doctor profiles";
    public static final String LOG_ALL_PROFILES_FETCHED = "All doctor profiles fetched successfully";
    public static final String CREATING_APPOINTMENT = "Creating a new appointment";
    public static final String APPOINTMENT_CREATED = "Successfully created appointment with ID: {}";
    public static final String FETCHING_UPCOMING_APPOINTMENTS = "Fetching upcoming appointments for doctor with ID: {}";
    public static final String UPCOMING_APPOINTMENTS_RETRIEVED = "Successfully retrieved upcoming appointments for doctor with ID: {}";
    public static final String CANCELLING_APPOINTMENT = "Cancelling appointment with ID: {}";
    public static final String APPOINTMENT_CANCELLED = "Successfully cancelled appointment with ID: {}";
    public static final String FETCHING_APPOINTMENT_HISTORY = "Fetching appointment history for doctor with ID: {}";
    public static final String APPOINTMENT_HISTORY_RETRIEVED = "Successfully retrieved appointment history for doctor with ID: {}";
    public static final String COMPLETING_APPOINTMENT = "Marking appointment with ID: {} as completed";
    public static final String APPOINTMENT_COMPLETED = "Successfully marked appointment with ID: {} as completed";
    public static final String CANCELLING_ALL_APPOINTMENTS = "Cancelling all appointments for doctor with ID: {} on date: {}";
    public static final String ALL_APPOINTMENTS_CANCELLED = "Successfully cancelled all appointments for doctor with ID: {} on date: {}";
    public static final String SUCCESSFUL_CREATION = "Successful creation";
    public static final String FAILED_CREATION = "Failed to create doctor profile";
    public static final String SUCCESSFUL_SAVE = "Default schedule saved successfully";
    public static final String FAILED_TO_SEND_MAIL = "Failed to send cancellation email";
    public static final String CANNOT_SAVE_DOCTOR_PROFILE = "Cannot save doctor profile";
    public static final String SUCCESSFUL_RETRIEVAL = "Successfully retrieved data";
    public static final String FETCHING_DOCTOR = "Fetching doctor with ID: {}";
    public static final String DOCTOR_FOUND = "Successfully found doctor with ID: {}";
    public static final String UPDATING_DOCTOR_PROFILE = "Updating profile for doctor with ID: {}";
    public static final String DOCTOR_PROFILE_UPDATED = "Successfully updated profile for doctor with ID: {}";
    public static final String NO_DOCTOR_PROFILES_FOUND = "No doctor profiles found";
    public static final String NO_SCHEDULE_FOUND_FOR_DAY = "No schedule found for %s.";
}
