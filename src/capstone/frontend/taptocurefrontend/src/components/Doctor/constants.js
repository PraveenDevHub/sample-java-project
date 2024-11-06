// src/components/Doctor/constants.js

export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
export const SAVE_DEFAULT_SCHEDULE_URL = `${API_BASE_URL}/savedefaultschedule`;
export const GET_APPOINTMENTS_URL = (doctorId) => `${API_BASE_URL}/getappointments/${doctorId}`;
export const CANCEL_APPOINTMENT_URL = (appointmentId) => `${API_BASE_URL}/appointment/${appointmentId}/cancel`;
export const COMPLETE_APPOINTMENT_URL = (appointmentId) => `${API_BASE_URL}/appointment/${appointmentId}/complete`;
export const CANCEL_ALL_APPOINTMENTS_URL = (doctorId) => `${API_BASE_URL}/appointment/${doctorId}/cancelAll`;
export const UPDATE_DOCTOR_PROFILE_URL = (doctorId) => `${API_BASE_URL}/doctor/updateDoctor/${doctorId}`;
export const GET_DOCTOR_PROFILE_URL = (doctorId) => `${API_BASE_URL}/doctorProfile/${doctorId}`;
export const GET_DEFAULT_SCHEDULE_URL = (doctorId, dayOfWeek) => `${API_BASE_URL}/defaultschedule/${doctorId}/${dayOfWeek}`;
export const UPDATE_SCHEDULE_URL = `${API_BASE_URL}/updateschedule`;

export const DAYS_OF_WEEK = [
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
  "Sunday",
];

export const TOAST_OPTIONS = {
  position: "bottom-left",
  autoClose: 5000,
};

export const DIALOG_MESSAGES = {
  REMOVE_TIME_BLOCK: "Are you sure you want to remove this time block?",
  CANCEL_APPOINTMENT: "Do you really want to cancel this appointment?",
  COMPLETE_APPOINTMENT: "Do you really want to mark this appointment as completed?",
  CANCEL_ALL_APPOINTMENTS: "Are you sure you want to cancel all appointments for the selected date?",
  SELECT_DATE_WARNING: "Please select a date.",
  REMOVE_TIME_BLOCK_CONFIRM: "Are you sure you want to remove this time block?",
};

export const BUTTON_LABELS = {
  ADD_TIME_BLOCK: "Add Time Block",
  SAVE: "Save",
  DISPLAY_ALL: "Display All",
  CANCEL_BY_DATE: "Cancel By Date",
  CONFIRM: "Confirm",
  CLOSE: "Close",
  CANCEL: "Cancel",
  COMPLETE: "Complete",
  YES_CANCEL_ALL: "Yes, Cancel All",
  NO_GO_BACK: "No, Go Back",
  EDIT_PROFILE: "Edit Profile",
  CHANGE_PHOTO: "Change Photo",
  YES: "Yes",
  NO: "No",
};

export const FORM_LABELS = {
  SELECT_DAY: "Select a day",
  START_TIME: "Start Time",
  END_TIME: "End Time",
  SLOT_DURATION: "Slot Duration (Minutes)",
  TIME_BLOCK: "Time Block",
  FILTER_BY_DATE: "Filter by Date:",
  SELECT_DATE_TO_CANCEL: "Select Date to Cancel All Appointments:",
  FULL_NAME: "Full Name",
  EMAIL: "Email",
  MEDICAL_LICENSE_NUMBER: "Medical License Number",
  SPECIALIZATION: "Specialization",
  YEARS_OF_EXPERIENCE: "Years of Experience",
  HOSPITAL_NAME: "Hospital Name",
  HOSPITAL_STATE: "Hospital State",
  HOSPITAL_CITY: "Hospital City",
  PROFILE_DESCRIPTION: "Profile Description",
  SELECT_STATE: "Select State",
  SELECT_CITY: "Select City",
  AVAILABLE_DATE: "Available Date",
  USE_DEFAULT_SCHEDULE: "Use Default Schedule",
};

export const TOAST_MESSAGES = {
  SAVE_SUCCESS: "Default schedule saved successfully!",
  SAVE_FAILURE: "Failed to save default schedule. Please try again.",
  FETCH_APPOINTMENTS_FAILURE: "Failed to fetch appointments",
  CANCEL_APPOINTMENT_SUCCESS: "Appointment cancelled successfully!",
  CANCEL_APPOINTMENT_FAILURE:
    "Failed to cancel the appointment. Please try again.",
  COMPLETE_APPOINTMENT_SUCCESS: "Appointment marked as completed successfully!",
  COMPLETE_APPOINTMENT_FAILURE:
    "Failed to complete the appointment. Please try again.",
  CANCEL_ALL_APPOINTMENTS_SUCCESS:
    "All appointments for the selected date have been cancelled successfully!",
  CANCEL_ALL_APPOINTMENTS_FAILURE:
    "Failed to cancel all appointments. Please try again.",
  PROFILE_UPDATE_SUCCESS: "Profile updated successfully!",
  PROFILE_UPDATE_FAILURE: "Error updating profile. Please try again.",
  UPDATE_AVAILABILITY_SUCCESS: "All time blocks updated successfully!",
  UPDATE_AVAILABILITY_FAILURE:
    "Failed to update availability. Please try again.",
  INVALID_TIME_BLOCK: "Invalid time block.",
  INVALID_SLOT_DURATION: "Slot duration exceeds the time block duration.",
  INVALID_PAST_TIME: "Cannot set schedule for past time.",

};

export const ERROR_MESSAGES = {
  DOCTOR_ID_NOT_FOUND: "Doctor ID not found. Please log in again.",
  NO_APPOINTMENTS: "No appointments available for the selected date.",
  FETCH_PROFILE_ERROR: "Error fetching the doctor profile",
  FETCH_DEFAULT_SCHEDULE_ERROR: "Error fetching default schedule",
  UPDATE_AVAILABILITY_ERROR: "Error updating availability",
};

export const TITLES = {
  SET_DEFAULT_SCHEDULE: "Set Default Schedule",
  CONFIRM_ACTION: "Confirm Action",
  CANCEL_APPOINTMENTS: "Cancelling Appointments",
  CANCELLED: "Cancelled!",
  COMPLETED: "Completed!",
  ERROR: "Error",
  WARNING: "Warning",
  EDIT_PROFILE: "Edit Profile",
  VIEW_PROFILE: "View Profile",
  SET_AVAILABILITY_SCHEDULE: "Set Availability Schedule",
};

export const TEXTS = {
  CANCEL_APPOINTMENT: "Cancelling appointment",
  COMPLETE_APPOINTMENT: "Completing appointment",
  CANCEL_ALL_APPOINTMENTS: "Please wait...",
  CANCEL_ALL_CONFIRM: "Are you sure you want to cancel all appointments for",
  NO_APPOINTMENTS: "No appointments available for the selected date.",
  FILTER_BY_DATE: "Filter by Date:",
  SELECT_DATE_TO_CANCEL: "Select Date to Cancel All Appointments:",
  LOADING: "Loading...",
  NO_PHOTO_AVAILABLE: "No Photo Available",
  PLEASE_WAIT_SAVING: "Please wait... Saving changes",
  GENERATING_TIME_SLOTS: "Generating time slots from:",
  TO: "to:",
  WITH_DURATION: "with duration:",
  START_TIME_AFTER_END_TIME: "Start time is equal to or after end time",
  GENERATED_SLOTS: "Generated slots:",
  TIME_BLOCK_WITH_SLOTS: "Time block with generated slots:",
  RESPONSE: "Response:",
};

export const LOG_MESSAGES = {
  DOCTOR_ID_FETCHED: "doctor id fetched: ",
  ERROR_CANCELLING_APPOINTMENT: "Error cancelling appointment:",
  ERROR_COMPLETING_APPOINTMENT: "Error completing appointment:",
  TIME_BLOCKS_WITH_SLOTS: "Time blocks with slots:",
  ERROR_UPDATING_PROFILE: "Error updating profile",
  ERROR_FETCHING_DEFAULT_SCHEDULE: "Error fetching default schedule:",
  ERROR_UPDATING_AVAILABILITY: "Error updating availability:",
};


export const APPOINTMENT_LABELS = {
  PATIENT: "Patient",
  DATE: "Date",
  TIME: "Time",
  REASON: "Reason",
  STATUS: "Status",
};


