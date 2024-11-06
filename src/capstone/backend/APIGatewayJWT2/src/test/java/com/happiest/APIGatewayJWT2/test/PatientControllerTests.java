package com.happiest.APIGatewayJWT2.test;

import com.happiest.APIGatewayJWT2.apigateway.PatientServiceInterface;
import com.happiest.APIGatewayJWT2.controller.PatientController;
import com.happiest.APIGatewayJWT2.model.Appointments;
import com.happiest.APIGatewayJWT2.model.Doctors;
import com.happiest.APIGatewayJWT2.model.Patients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientControllerTests {

    @Mock
    private PatientServiceInterface patientServiceInterface;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDoctors() {
        List<Doctors> doctors = Arrays.asList(new Doctors(), new Doctors());
        when(patientServiceInterface.getDoctors(anyInt())).thenReturn(doctors);

        List<Doctors> result = patientController.getDoctors(10);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(patientServiceInterface, times(1)).getDoctors(anyInt());
    }

    @Test
    public void testFilterDoctors() {
        List<Doctors> doctors = Arrays.asList(new Doctors(), new Doctors());
        when(patientServiceInterface.filterDoctors(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(ResponseEntity.ok(doctors));

        ResponseEntity<List<Doctors>> response = patientController.filterDoctors("state", "city", "specialization", "hospital", "searchTerm");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(patientServiceInterface, times(1)).filterDoctors(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetPatientById() {
        Patients patient = new Patients();
        when(patientServiceInterface.getPatientById(anyInt())).thenReturn(ResponseEntity.ok(patient));

        ResponseEntity<Patients> response = patientController.getPatientById(1);

        assertNotNull(response);
        assertEquals(patient, response.getBody());
        verify(patientServiceInterface, times(1)).getPatientById(anyInt());
    }

    @Test
    public void testCreateAppointment() {
        Patients patient = new Patients();
        patient.setPatientId(1);
        Appointments appointment = new Appointments();
        appointment.setPatient(patient);

        when(patientServiceInterface.createAppointment(any(Appointments.class))).thenReturn(ResponseEntity.ok(appointment));

        ResponseEntity<Appointments> response = patientController.createAppointment(appointment);

        assertNotNull(response);
        assertEquals(appointment, response.getBody());
        verify(patientServiceInterface, times(1)).createAppointment(any(Appointments.class));
    }

    @Test
    public void testGetAppointmentsByPatientId() {
        List<Appointments> appointments = Arrays.asList(new Appointments(), new Appointments());
        when(patientServiceInterface.getAppointmentsByPatientId(anyInt())).thenReturn(ResponseEntity.ok(appointments));

        ResponseEntity<List<Appointments>> response = patientController.getAppointmentsByPatientId(1);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientServiceInterface, times(1)).getAppointmentsByPatientId(anyInt());
    }

    @Test
    public void testGetAvailableDates() {
        List<LocalDate> dates = Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1));
        when(patientServiceInterface.getAvailableDates(anyInt())).thenReturn(ResponseEntity.ok(dates));

        ResponseEntity<List<LocalDate>> response = patientController.getAvailableDates(1);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientServiceInterface, times(1)).getAvailableDates(anyInt());
    }

    @Test
    public void testGetAvailableSlots() {
        List<LocalTime> slots = Arrays.asList(LocalTime.now(), LocalTime.now().plusHours(1));
        when(patientServiceInterface.getAvailableSlots(anyInt(), anyString())).thenReturn(ResponseEntity.ok(slots));

        ResponseEntity<List<LocalTime>> response = patientController.getAvailableSlots(1, "2023-10-30");

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientServiceInterface, times(1)).getAvailableSlots(anyInt(), anyString());
    }

    @Test
    public void testBookAppointment() {
        Patients patient = new Patients();
        patient.setPatientId(1);
        Appointments appointment = new Appointments();
        appointment.setPatient(patient);

        when(patientServiceInterface.bookAppointment(any(Appointments.class))).thenReturn(ResponseEntity.ok("Appointment booked"));

        ResponseEntity<String> response = patientController.bookAppointment(appointment);

        assertNotNull(response);
        assertEquals("Appointment booked", response.getBody());
        verify(patientServiceInterface, times(1)).bookAppointment(any(Appointments.class));
    }

    @Test
    public void testGetPatientProfile() {
        Patients patient = new Patients();
        when(patientServiceInterface.getPatientProfile(anyInt())).thenReturn(ResponseEntity.ok(patient));

        ResponseEntity<Patients> response = patientController.getPatientProfile(1);

        assertNotNull(response);
        assertEquals(patient, response.getBody());
        verify(patientServiceInterface, times(1)).getPatientProfile(anyInt());
    }

    @Test
    public void testUpdatePatientProfile() {
        Patients updatedPatient = new Patients();
        when(patientServiceInterface.updatePatientProfile(anyInt(), any(Patients.class))).thenReturn(ResponseEntity.ok(updatedPatient));

        ResponseEntity<Patients> response = patientController.updatePatientProfile(1, updatedPatient);

        assertNotNull(response);
        assertEquals(updatedPatient, response.getBody());
        verify(patientServiceInterface, times(1)).updatePatientProfile(anyInt(), any(Patients.class));
    }
}
