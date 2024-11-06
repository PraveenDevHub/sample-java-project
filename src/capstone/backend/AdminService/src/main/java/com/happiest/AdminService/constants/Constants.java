package com.happiest.AdminService.constants;

import com.happiest.AdminService.model.Doctors;

import static com.happiest.AdminService.model.Doctors.ApprovalStatus.Pending;

public class Constants {
    // Logging messages
    public static final String LOG_FETCH_DASHBOARD_COUNTS = "adminservice.dashboard.fetching";
    public static final String LOG_FETCH_DASHBOARD_SUCCESS = "adminservice.dashboard.success";
    public static final String LOG_FETCH_DASHBOARD_FAILURE = "adminservice.dashboard.failure";

    // Exception messages
    public static final String ERROR_FETCH_DASHBOARD_COUNTS = "adminservice.error.dashboardCounts";

    // Entity keys (internal use)
    public static final String ENTITY_USERS = "users";
    public static final String ENTITY_PATIENTS = "patients";
    public static final String ENTITY_DOCTORS = "doctors";
    public static final String ENTITY_APPOINTMENTS = "appointments";
    public static final String ENTITY_APPROVALS = "approvals";

    // Approval status (internal use)
    public static final Doctors.ApprovalStatus APPROVAL_STATUS_PENDING = Pending; // Internal status key

    // Logging messages
    public static final String LOG_FETCH_DOCTORS_WITH_STATUS = "doctorservice.fetch.doctors.status";
    public static final String LOG_FETCH_ALL_DOCTORS = "doctorservice.fetch.all";
    public static final String LOG_FETCH_PENDING_APPROVALS = "doctorservice.fetch.pending.approvals";
    public static final String LOG_DOCTOR_NOT_FOUND = "doctorservice.error.notfound";
    public static final String LOG_UPDATE_DOCTOR_STATUS = "doctorservice.update.status";
    public static final String LOG_EMAIL_NOTIFICATION_SENT = "doctorservice.email.sent";

    // Exception messages
    public static final String ERROR_NO_DOCTORS_FOUND = "doctorservice.error.noDoctorsFound";
    public static final String ERROR_INVALID_OPERATION = "doctorservice.error.invalidOperation";

    // Entity keys (internal use)
    public static final String ENTITY_DOCTOR_ID = "doctorId";

    // Approval status (internal use)
//    public static final Doctors.ApprovalStatus APPROVAL_STATUS_PENDING = Pending; // Internal status key

    // Email constants
    public static final String EMAIL_SUBJECT_DOCTOR_APPROVAL_STATUS = "Doctor Approval Status";
    public static final String EMAIL_MESSAGE_DOCTOR_APPROVAL_TEMPLATE = "Dear Dr. %s, your registration has been %s.";

    public static final String EMAIL_SEND_START = "emailservice.send.start";
    public static final String EMAIL_SEND_SUCCESS = "emailservice.send.success";
    public static final String EMAIL_SEND_FAILURE = "emailservice.send.failure";

    // Exception related constants
    public static final String EMAIL_SEND_ERROR = "emailservice.error.emailSend";

    // EmailService related constants
    // PatientService related constants
    public static final String PATIENT_FETCH_SUCCESS = "patientservice.fetch.success";
    public static final String PATIENT_FETCH_FAILURE = "patientservice.fetch.failure";

    public static final String ADMIN_DASHBOARD_COUNTS_FETCH_START = "adminservice.dashboard.counts.fetch.start";
    public static final String ADMIN_DASHBOARD_COUNTS_FETCH_SUCCESS = "adminservice.dashboard.counts.fetch.success";

    // Doctor Approvals
    public static final String DOCTOR_APPROVALS_PENDING_FETCH_START = "adminservice.doctor.approvals.pending.fetch.start";
    public static final String DOCTOR_APPROVALS_PENDING_FETCH_SUCCESS = "adminservice.doctor.approvals.pending.fetch.success";
    public static final String DOCTOR_APPROVE_START = "adminservice.doctor.approve.start";
    public static final String DOCTOR_APPROVE_SUCCESS = "adminservice.doctor.approve.success";
    public static final String DOCTOR_REJECT_START = "adminservice.doctor.reject.start";
    public static final String DOCTOR_REJECT_SUCCESS = "adminservice.doctor.reject.success";

    // Response messages
    public static final String DOCTOR_APPROVE_RESPONSE = "doctor.approval.response";
    public static final String DOCTOR_REJECT_RESPONSE = "doctor.rejection.response";

    // DoctorController related constants
    public static final String DOCTOR_FETCH_START = "doctorservice.doctors.fetch.start";
    public static final String DOCTOR_FETCH_SUCCESS = "doctorservice.doctors.fetch.success";

    // PatientController related constants
    public static final String PATIENT_FETCH_START = "patientservice.patients.fetch.start";
//    public static final String PATIENT_FETCH_SUCCESS = "patientservice.patients.fetch.success";


}
