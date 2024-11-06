package com.happiest.APIGatewayJWT2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happiest.APIGatewayJWT2.apigateway.*;
import com.happiest.APIGatewayJWT2.constants.LoggerConstants;
import com.happiest.APIGatewayJWT2.constants.UserControllerConstants;
import com.happiest.APIGatewayJWT2.dto.DoctorDTO;
import com.happiest.APIGatewayJWT2.dto.PatientDTO;
import com.happiest.APIGatewayJWT2.model.*;
import com.happiest.APIGatewayJWT2.repository.UserRepo;
import com.happiest.APIGatewayJWT2.service.EmailService;
import com.happiest.APIGatewayJWT2.service.UserService;
import com.happiest.APIGatewayJWT2.model.DoctorProfile;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private EmailService emailService;

    @Autowired
    DoctorserviceInterface doctorserviceInterface;

    @Autowired
    PatientServiceInterface patientServiceInterface;

    @Autowired
    AdminServiceInterface adminServiceInterface;

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) {
        Users user = new ObjectMapper().convertValue(payload.get("user"), Users.class);
        Object roleSpecificData = payload.get("roleSpecificData");

        logger.info(LoggerConstants.REGISTERING_USER, user.getEmail());
        service.register(user, roleSpecificData);
        logger.info(LoggerConstants.USER_REGISTERED_SUCCESSFULLY, user.getEmail());
        return ResponseEntity.status(201).body(UserControllerConstants.USER_REGISTERED_SUCCESSFULLY_MSG);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        logger.info(LoggerConstants.LOGIN_ATTEMPT, user.getEmail());
        Map<String, Object> result = service.verify(user);
        logger.info(LoggerConstants.LOGIN_SUCCESSFUL, user.getEmail());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        logger.info(LoggerConstants.PASSWORD_RESET_REQUESTED, email);
        service.generatePasswordResetToken(email);
        return ResponseEntity.ok(UserControllerConstants.PASSWORD_RESET_EMAIL_SENT);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        logger.info(LoggerConstants.RESETTING_PASSWORD, token);
        service.resetPassword(token, newPassword);
        logger.info(LoggerConstants.PASSWORD_RESET_SUCCESSFUL, token);
        return ResponseEntity.ok(UserControllerConstants.PASSWORD_RESET_SUCCESSFUL_MSG);
    }

    @GetMapping("/check-verification")
    public ResponseEntity<?> checkVerification(@RequestParam String email) {
        logger.info(LoggerConstants.CHECKING_VERIFICATION_STATUS, email);
        Optional<Users> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return ResponseEntity.ok(user.isEnabled());
        } else {
            logger.warn(LoggerConstants.USER_NOT_FOUND, email);
            return ResponseEntity.status(404).body(UserControllerConstants.USER_NOT_FOUND_MSG);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token, HttpServletResponse response) {
        logger.info(LoggerConstants.VERIFYING_USER, token);
        boolean isVerified = service.verifyEmail(token);  // Throws InvalidTokenException if token is invalid or expired
        if (isVerified) {
            try {
                response.sendRedirect(UserControllerConstants.LOGIN_URL);
            } catch (IOException e) {
                logger.error(LoggerConstants.ERROR_REDIRECTING_TO_LOGIN, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(UserControllerConstants.ERROR_REDIRECTING_TO_LOGIN_MSG);
            }
            logger.info(LoggerConstants.USER_VERIFIED_SUCCESSFULLY, token);
            return ResponseEntity.ok("User verified successfully");
        } else {
            logger.warn(LoggerConstants.INVALID_OR_EXPIRED_TOKEN, token);
            return ResponseEntity.badRequest().body(UserControllerConstants.INVALID_OR_EXPIRED_TOKEN_MSG);
        }
    }

    @GetMapping("/test-email")
    public String testEmail() {
        logger.info(LoggerConstants.SENDING_TEST_EMAIL);
        emailService.sendEmail("praveenk2622@gmail.com", "Test Email", "This is a test email.");
        return "Test email sent";
    }

    @PostMapping("/sendVerificationEmail")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Users user) {
        logger.info(LoggerConstants.RECEIVED_REQUEST_TO_SEND_VERIFICATION_EMAIL, user.getEmail());
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + UserControllerConstants.TOKEN_EXPIRY_DURATION)); // 1 hour expiry
        service.saveVerificationToken(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), token);
        logger.info(LoggerConstants.VERIFICATION_EMAIL_SENT, user.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateschedule")
    public ResponseEntity<String> updateSchedule(@RequestBody DoctorProfile doctorProfile) {
        logger.info(LoggerConstants.UPDATING_SCHEDULE, doctorProfile.getDoctor().getDoctorId());
        ResponseEntity<?> response = doctorserviceInterface.createDoctorProfile(doctorProfile);
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            logger.warn(LoggerConstants.SCHEDULE_CONFLICT, doctorProfile.getDoctor().getDoctorId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(UserControllerConstants.SCHEDULE_CONFLICT_MSG);
        }
        logger.info(LoggerConstants.SCHEDULE_UPDATED_SUCCESSFULLY, doctorProfile.getDoctor().getDoctorId());
        return ResponseEntity.ok(UserControllerConstants.SCHEDULE_UPDATED_SUCCESSFULLY_MSG);
    }

    @PostMapping("/savedefaultschedule")
    public ResponseEntity<String> saveDefaultSchedule(@RequestBody Map<String, Object> defaultSchedule) {
        logger.info(LoggerConstants.SAVING_DEFAULT_SCHEDULE);
        doctorserviceInterface.saveDefaultSchedule(defaultSchedule);
        logger.info(LoggerConstants.DEFAULT_SCHEDULE_SAVED_SUCCESSFULLY);
        return ResponseEntity.ok(UserControllerConstants.DEFAULT_SCHEDULE_SAVED_SUCCESSFULLY_MSG);
    }

    @GetMapping("/defaultschedule/{doctorId}/{dayOfWeek}")
    public List<DefaultSchedule> getDefaultScheduleForDay(@PathVariable Integer doctorId, @PathVariable String dayOfWeek) {
        logger.info(LoggerConstants.FETCHING_DEFAULT_SCHEDULE, doctorId, dayOfWeek);
        return doctorserviceInterface.getDefaultScheduleForDay(doctorId, dayOfWeek);
    }

    @GetMapping("/getschedule")
    public ResponseEntity<List<DoctorProfile>> getAllDoctorProfiles() {
        logger.info(LoggerConstants.FETCHING_ALL_DOCTOR_PROFILES);
        List<DoctorProfile> profiles = doctorserviceInterface.getAllDoctorProfiles();
        return ResponseEntity.ok(profiles);
    }

    @PostMapping("/appointment")
    public ResponseEntity<Appointments> createAppointment(@RequestBody Appointments appointment) {
        logger.info(LoggerConstants.CREATING_APPOINTMENT, appointment.getPatient().getPatientId());
        return doctorserviceInterface.createAppointment(appointment);
    }

    @GetMapping("/getappointments/{doctorId}")
    public ResponseEntity<List<Appointments>> getUpcomingAppointments(@PathVariable int doctorId) {
        logger.info(LoggerConstants.FETCHING_UPCOMING_APPOINTMENTS, doctorId);
        return doctorserviceInterface.getUpcomingAppointments(doctorId);
    }

    @PutMapping("/appointment/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable int appointmentId) {
        logger.info(LoggerConstants.CANCELLING_APPOINTMENT, appointmentId);
        return doctorserviceInterface.cancelAppointment(appointmentId);
    }

    @PutMapping("/appointment/{appointmentId}/complete")
    public ResponseEntity<Void> completeAppointment(@PathVariable int appointmentId) {
        logger.info(LoggerConstants.COMPLETING_APPOINTMENT, appointmentId);
        return doctorserviceInterface.completeAppointment(appointmentId);
    }

    @GetMapping("/appointment/doctors/history/{doctorId}")
    public ResponseEntity<List<Appointments>> getDoctorAppointmentHistory(@PathVariable int doctorId) {
        logger.info(LoggerConstants.FETCHING_DOCTOR_APPOINTMENT_HISTORY, doctorId);
        return doctorserviceInterface.getDoctorAppointmentHistory(doctorId);
    }

    @PutMapping("appointment/{doctorId}/cancelAll")
    public ResponseEntity<String> cancelAllAppointments(@PathVariable Integer doctorId, @RequestBody Map<String, String> request) {
        logger.info(LoggerConstants.CANCELLING_ALL_APPOINTMENTS, doctorId);
        return doctorserviceInterface.cancelAllAppointments(doctorId, request);
    }

    @GetMapping("/appointment/history/{patientId}")
    public ResponseEntity<List<Appointments>> getAppointmentHistory(@PathVariable int patientId) {
        logger.info(LoggerConstants.FETCHING_APPOINTMENT_HISTORY, patientId);
        return patientServiceInterface.getAppointmentHistory(patientId);
    }

    @PutMapping("/appointment/cancel/{appointmentId}/{patientId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable int appointmentId, @PathVariable int patientId) {
        logger.info(LoggerConstants.CANCELLING_APPOINTMENT_FOR_PATIENT, appointmentId, patientId);
        return patientServiceInterface.cancelAppointment(appointmentId, patientId);
    }

    @GetMapping("/appointment/upcoming/{patientId}")
    public ResponseEntity<List<Appointments>> getPatientUpcomingAppointments(@PathVariable int patientId) {
        logger.info(LoggerConstants.FETCHING_UPCOMING_APPOINTMENTS_FOR_PATIENT, patientId);
        return patientServiceInterface.getPatientUpcomingAppointments(patientId);
    }

    @GetMapping("admin/dashboard-counts")
    public ResponseEntity<Map<String, Long>> getDashboardCounts() {
        logger.info(LoggerConstants.FETCHING_DASHBOARD_COUNTS);
        return adminServiceInterface.getDashboardCounts();
    }

    @GetMapping("admin/appointment-statistics")
    public ResponseEntity<Map<String, Object>> getAppointmentStatistics() {
        logger.info(LoggerConstants.FETCHING_APPOINTMENT_STATISTICS);
        return adminServiceInterface.getAppointmentStatistics();
    }

    @GetMapping("admin/pending-approvals")
    public ResponseEntity<List<Doctors>> getPendingDoctors() {
        logger.info(LoggerConstants.FETCHING_PENDING_DOCTOR_APPROVALS);
        return adminServiceInterface.getPendingDoctors();
    }

    @PutMapping("admin/approve-doctor/{doctorId}")
    public ResponseEntity<String> approveDoctor(@PathVariable Integer doctorId) {
        logger.info(LoggerConstants.APPROVING_DOCTOR, doctorId);
        return adminServiceInterface.approveDoctor(doctorId);
    }

    @PutMapping("admin/reject-doctor/{doctorId}")
    public ResponseEntity<String> rejectDoctor(@PathVariable Integer doctorId) {
        logger.info(LoggerConstants.REJECTING_DOCTOR, doctorId);
        return adminServiceInterface.rejectDoctor(doctorId);
    }

    @GetMapping("/doctorsdisplay/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(@RequestParam(required = false) Doctors.ApprovalStatus approvalStatus) {
        logger.info(LoggerConstants.FETCHING_ALL_DOCTORS, approvalStatus);
        return adminServiceInterface.getAllDoctors(approvalStatus);
    }

    @GetMapping("/patientsdisplay/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        logger.info(LoggerConstants.FETCHING_ALL_PATIENTS);
        return adminServiceInterface.getAllPatients();
    }

    @GetMapping("/doctorProfile/{doctorId}")
    public ResponseEntity<Doctors> getDoctorById(@PathVariable Integer doctorId) {
        logger.info(LoggerConstants.FETCHING_DOCTOR_PROFILE, doctorId);
        return doctorserviceInterface.getDoctorById(doctorId);
    }

    @GetMapping("/getUserDetails/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer userId) {
        logger.info(LoggerConstants.FETCHING_USER_DETAILS, userId);
        return doctorserviceInterface.getUserById(userId);
    }
}

