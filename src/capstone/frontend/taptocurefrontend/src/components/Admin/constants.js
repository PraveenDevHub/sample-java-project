// src/components/AdminPanel/constants.js
export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
export const APPROVE_DOCTOR_URL = process.env.REACT_APP_APPROVE_DOCTOR_URL;
export const REJECT_DOCTOR_URL = process.env.REACT_APP_REJECT_DOCTOR_URL;
export const DASHBOARD_COUNTS_URL = `${API_BASE_URL}/admin/dashboard-counts`;
export const APPOINTMENT_STATS_URL = `${API_BASE_URL}/admin/appointment-statistics`;

export const MESSAGES = {
  APPROVE_CONFIRM: "Are you sure you want to approve this doctor?",
  REJECT_CONFIRM: "Are you sure you want to reject this doctor?",
  APPROVE_SUCCESS: "Doctor approved successfully",
  APPROVE_FAILURE: "Failed to approve doctor",
  REJECT_SUCCESS: "Doctor rejected successfully",
  REJECT_FAILURE: "Failed to reject doctor",
  FETCH_ERROR: "Failed to fetch pending approvals",
  NO_RESPONSE: "No response from the server. Please check your connection.",
  UNEXPECTED_ERROR: "An unexpected error occurred.",
  FETCH_STATS_ERROR: "Error fetching statistics",
  FETCH_APPOINTMENT_STATS_ERROR: "Error fetching appointment statistics",
};

export const LABELS = {
  ADMIN_PANEL: "Admin Panel",
  DASHBOARD: "Dashboard",
  PATIENTS: "Patients",
  DOCTORS: "Doctors",
  PENDING_APPROVALS: "Pending Approvals",
  STATISTICS: "Statistics",
  PENDING_DOCTOR_APPROVALS: "Pending Doctor Approvals",
  VIEW_DETAILS: "View Details",
  BACK: "Back",
  APPROVE: "Approve",
  REJECT: "Reject",
  CONFIRM_ACTION: "Confirm Action",
  YES: "Yes",
  NO: "No",
  SPECIALIZATION: "Specialization",
  LOCATION: "Location",
  EMAIL: "Email",
  MEDICAL_LICENSE: "Medical License",
  YEARS_OF_EXPERIENCE: "Years of Experience",
  HOSPITAL: "Hospital",
  ADMIN_DASHBOARD_STATS: "Admin Dashboard Statistics",
  DAILY_APPOINTMENTS: "Daily Appointments",
  MONTHLY_APPOINTMENTS: "Monthly Appointments",
  NUMBER_OF_APPOINTMENTS: "Number of Appointments",
  COUNT_BAR: "Count (Bar)",
  USERS: "Users",
  PATIENTS: "Patients",
  DOCTORS: "Doctors",
  APPOINTMENTS: "Appointments",
  PENDING_APPROVALS: "Pending Approvals",
};

export const PATHS = {
  ADMIN_HOME: "/adminhome",
  PATIENTS: "/patients",
  DOCTORS: "/doctors",
  PENDING_APPROVALS: "/pending-approvals",
  STATISTICS: "/statistics",
};
