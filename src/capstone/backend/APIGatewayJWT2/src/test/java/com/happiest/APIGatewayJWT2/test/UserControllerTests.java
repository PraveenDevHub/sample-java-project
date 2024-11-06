package com.happiest.APIGatewayJWT2.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happiest.APIGatewayJWT2.apigateway.AdminServiceInterface;
import com.happiest.APIGatewayJWT2.apigateway.DoctorserviceInterface;
import com.happiest.APIGatewayJWT2.apigateway.PatientServiceInterface;
import com.happiest.APIGatewayJWT2.constants.UserControllerConstants;
import com.happiest.APIGatewayJWT2.controller.UserController;
import com.happiest.APIGatewayJWT2.dto.DoctorDTO;
import com.happiest.APIGatewayJWT2.dto.PatientDTO;
import com.happiest.APIGatewayJWT2.model.*;
import com.happiest.APIGatewayJWT2.repository.UserRepo;
import com.happiest.APIGatewayJWT2.service.EmailService;
import com.happiest.APIGatewayJWT2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    private DoctorserviceInterface doctorserviceInterface;

    @Mock
    private PatientServiceInterface patientServiceInterface;

    @Mock
    private AdminServiceInterface adminServiceInterface;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        Map<String, Object> payload = new HashMap<>();
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setRole(Users.Role.Patient); // Set role to Patient for this test
        payload.put("user", user);
        payload.put("roleSpecificData", new Object());

        // Mock the register method to return the user
        when(userService.register(any(Users.class), any())).thenReturn(user);

        ResponseEntity<?> response = userController.register(payload);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(UserControllerConstants.USER_REGISTERED_SUCCESSFULLY_MSG, response.getBody());
        verify(userService, times(1)).register(any(Users.class), any());
    }



    @Test
    public void testLogin() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Map<String, Object> result = new HashMap<>();
        result.put("token", "jwt-token");

        when(userService.verify(any(Users.class))).thenReturn(result);

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(result, response.getBody());
        verify(userService, times(1)).verify(any(Users.class));
    }

    @Test
    public void testForgotPassword() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");

        doNothing().when(userService).generatePasswordResetToken(anyString());

        ResponseEntity<?> response = userController.forgotPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserControllerConstants.PASSWORD_RESET_EMAIL_SENT, response.getBody());
        verify(userService, times(1)).generatePasswordResetToken(anyString());
    }

    @Test
    public void testResetPassword() {
        Map<String, String> request = new HashMap<>();
        request.put("token", "reset-token");
        request.put("newPassword", "new-password");

        doNothing().when(userService).resetPassword(anyString(), anyString());

        ResponseEntity<?> response = userController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserControllerConstants.PASSWORD_RESET_SUCCESSFUL_MSG, response.getBody());
        verify(userService, times(1)).resetPassword(anyString(), anyString());
    }

    @Test
    public void testCheckVerification_UserExists() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setEnabled(true);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.checkVerification("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody());
        verify(userRepo, times(1)).findByEmail(anyString());
    }

    @Test
    public void testCheckVerification_UserNotFound() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.checkVerification("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(UserControllerConstants.USER_NOT_FOUND_MSG, response.getBody());
        verify(userRepo, times(1)).findByEmail(anyString());
    }

    @Test
    public void testVerifyUser_Success() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userService.verifyEmail(anyString())).thenReturn(true);

        ResponseEntity<String> result = userController.verifyUser("valid-token", response);

        verify(response, times(1)).sendRedirect(UserControllerConstants.LOGIN_URL);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User verified successfully", result.getBody());
    }

    @Test
    public void testVerifyUser_InvalidToken() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userService.verifyEmail(anyString())).thenReturn(false);

        ResponseEntity<String> result = userController.verifyUser("invalid-token", response);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(UserControllerConstants.INVALID_OR_EXPIRED_TOKEN_MSG, result.getBody());
    }

    @Test
    public void testTestEmail() {
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        String result = userController.testEmail();

        assertEquals("Test email sent", result);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testSendVerificationEmail() {
        Users user = new Users();
        user.setEmail("test@example.com");

        doNothing().when(userService).saveVerificationToken(any(VerificationToken.class));
        doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

        ResponseEntity<?> response = userController.sendVerificationEmail(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).saveVerificationToken(any(VerificationToken.class));
        verify(emailService, times(1)).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    public void testUpdateSchedule() {
        DoctorProfile doctorProfile = new DoctorProfile();
        Doctors doctor = new Doctors();
        doctor.setDoctorId(1);
        doctorProfile.setDoctor(doctor);

        when(doctorserviceInterface.createDoctorProfile(any(DoctorProfile.class))).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<String> response = userController.updateSchedule(doctorProfile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserControllerConstants.SCHEDULE_UPDATED_SUCCESSFULLY_MSG, response.getBody());
        verify(doctorserviceInterface, times(1)).createDoctorProfile(any(DoctorProfile.class));
    }

    @Test
    public void testSaveDefaultSchedule() {
        Map<String, Object> defaultSchedule = new HashMap<>();

        doNothing().when(doctorserviceInterface).saveDefaultSchedule(anyMap());

        ResponseEntity<String> response = userController.saveDefaultSchedule(defaultSchedule);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserControllerConstants.DEFAULT_SCHEDULE_SAVED_SUCCESSFULLY_MSG, response.getBody());
        verify(doctorserviceInterface, times(1)).saveDefaultSchedule(anyMap());
    }

    @Test
    public void testGetDefaultScheduleForDay() {
        List<DefaultSchedule> schedules = Arrays.asList(new DefaultSchedule(), new DefaultSchedule());

        when(doctorserviceInterface.getDefaultScheduleForDay(anyInt(), anyString())).thenReturn(schedules);

        List<DefaultSchedule> result = userController.getDefaultScheduleForDay(1, "Monday");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(doctorserviceInterface, times(1)).getDefaultScheduleForDay(anyInt(), anyString());
    }

    @Test
    public void testGetAllDoctorProfiles() {
        List<DoctorProfile> profiles = Arrays.asList(new DoctorProfile(), new DoctorProfile());

        when(doctorserviceInterface.getAllDoctorProfiles()).thenReturn(profiles);

        ResponseEntity<List<DoctorProfile>> response = userController.getAllDoctorProfiles();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(doctorserviceInterface, times(1)).getAllDoctorProfiles();
    }

    @Test
    public void testCreateAppointment() {
        Patients patient = new Patients();
        patient.setPatientId(1);
        Appointments appointment = new Appointments();
        appointment.setPatient(patient);

        when(doctorserviceInterface.createAppointment(any(Appointments.class))).thenReturn(ResponseEntity.ok(appointment));

        ResponseEntity<Appointments> response = userController.createAppointment(appointment);

        assertNotNull(response);
        assertEquals(appointment, response.getBody());
        verify(doctorserviceInterface, times(1)).createAppointment(any(Appointments.class));
    }

    @Test
    public void testGetUpcomingAppointments() {
        List<Appointments> appointments = Arrays.asList(new Appointments(), new Appointments());

        when(doctorserviceInterface.getUpcomingAppointments(anyInt())).thenReturn(ResponseEntity.ok(appointments));

        ResponseEntity<List<Appointments>> response = userController.getUpcomingAppointments(1);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(doctorserviceInterface, times(1)).getUpcomingAppointments(anyInt());
    }

//    @Test
//    public void testCancelAppointment() {
//        doNothing().when(doctorserviceInterface).cancelAppointment(anyInt());
//
//        ResponseEntity<Void> response = userController.cancelAppointment(1);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(doctorserviceInterface, times(1)).cancelAppointment(anyInt());
//    }

    @Test
    public void testGetDoctorAppointmentHistory() {
        List<Appointments> appointments = Arrays.asList(new Appointments(), new Appointments());

        when(doctorserviceInterface.getDoctorAppointmentHistory(anyInt())).thenReturn(ResponseEntity.ok(appointments));

        ResponseEntity<List<Appointments>> response = userController.getDoctorAppointmentHistory(1);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(doctorserviceInterface, times(1)).getDoctorAppointmentHistory(anyInt());
    }

    @Test
    public void testCancelAllAppointments() {
        Map<String, String> request = new HashMap<>();
        request.put("reason", "Doctor unavailable");

        when(doctorserviceInterface.cancelAllAppointments(anyInt(), anyMap())).thenReturn(ResponseEntity.ok("All appointments cancelled"));

        ResponseEntity<String> response = userController.cancelAllAppointments(1, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All appointments cancelled", response.getBody());
        verify(doctorserviceInterface, times(1)).cancelAllAppointments(anyInt(), anyMap());
    }

    @Test
    public void testGetAppointmentHistory() {
        List<Appointments> appointments = Arrays.asList(new Appointments(), new Appointments());

        when(patientServiceInterface.getAppointmentHistory(anyInt())).thenReturn(ResponseEntity.ok(appointments));

        ResponseEntity<List<Appointments>> response = userController.getAppointmentHistory(1);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientServiceInterface, times(1)).getAppointmentHistory(anyInt());
    }

    @Test
    public void testGetPatientUpcomingAppointments() {
        List<Appointments> appointments = Arrays.asList(new Appointments(), new Appointments());

        when(patientServiceInterface.getPatientUpcomingAppointments(anyInt())).thenReturn(ResponseEntity.ok(appointments));

        ResponseEntity<List<Appointments>> response = userController.getPatientUpcomingAppointments(1);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientServiceInterface, times(1)).getPatientUpcomingAppointments(anyInt());
    }

    @Test
    public void testGetDashboardCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("doctors", 10L);
        counts.put("patients", 20L);

        when(adminServiceInterface.getDashboardCounts()).thenReturn(ResponseEntity.ok(counts));

        ResponseEntity<Map<String, Long>> response = userController.getDashboardCounts();

        assertNotNull(response);
        assertEquals(counts, response.getBody());
        verify(adminServiceInterface, times(1)).getDashboardCounts();
    }

    @Test
    public void testGetAppointmentStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalAppointments", 100);

        when(adminServiceInterface.getAppointmentStatistics()).thenReturn(ResponseEntity.ok(statistics));

        ResponseEntity<Map<String, Object>> response = userController.getAppointmentStatistics();

        assertNotNull(response);
        assertEquals(statistics, response.getBody());
        verify(adminServiceInterface, times(1)).getAppointmentStatistics();
    }

    @Test
    public void testGetPendingDoctors() {
        List<Doctors> doctors = Arrays.asList(new Doctors(), new Doctors());

        when(adminServiceInterface.getPendingDoctors()).thenReturn(ResponseEntity.ok(doctors));

        ResponseEntity<List<Doctors>> response = userController.getPendingDoctors();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(adminServiceInterface, times(1)).getPendingDoctors();
    }

    @Test
    public void testApproveDoctor() {
        when(adminServiceInterface.approveDoctor(anyInt())).thenReturn(ResponseEntity.ok("Doctor approved"));

        ResponseEntity<String> response = userController.approveDoctor(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doctor approved", response.getBody());
        verify(adminServiceInterface, times(1)).approveDoctor(anyInt());
    }

    @Test
    public void testRejectDoctor() {
        when(adminServiceInterface.rejectDoctor(anyInt())).thenReturn(ResponseEntity.ok("Doctor rejected"));

        ResponseEntity<String> response = userController.rejectDoctor(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doctor rejected", response.getBody());
        verify(adminServiceInterface, times(1)).rejectDoctor(anyInt());
    }

    @Test
    public void testGetAllDoctors() {
        List<DoctorDTO> doctors = Arrays.asList(new DoctorDTO(), new DoctorDTO());

        when(adminServiceInterface.getAllDoctors(any())).thenReturn(ResponseEntity.ok(doctors));

        ResponseEntity<List<DoctorDTO>> response = userController.getAllDoctors(null);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(adminServiceInterface, times(1)).getAllDoctors(any());
    }

    @Test
    public void testGetAllPatients() {
        List<PatientDTO> patients = Arrays.asList(new PatientDTO(), new PatientDTO());

        when(adminServiceInterface.getAllPatients()).thenReturn(ResponseEntity.ok(patients));

        ResponseEntity<List<PatientDTO>> response = userController.getAllPatients();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(adminServiceInterface, times(1)).getAllPatients();
    }

    @Test
    public void testGetDoctorById() {
        Doctors doctor = new Doctors();
        when(doctorserviceInterface.getDoctorById(anyInt())).thenReturn(ResponseEntity.ok(doctor));

        ResponseEntity<Doctors> response = userController.getDoctorById(1);

        assertNotNull(response);
        assertEquals(doctor, response.getBody());
        verify(doctorserviceInterface, times(1)).getDoctorById(anyInt());
    }

    @Test
    public void testGetUserById() {
        Users user = new Users();
        when(doctorserviceInterface.getUserById(anyInt())).thenReturn(ResponseEntity.ok(user));

        ResponseEntity<Users> response = userController.getUserById(1);

        assertNotNull(response);
        assertEquals(user, response.getBody());
        verify(doctorserviceInterface, times(1)).getUserById(anyInt());
    }
}
