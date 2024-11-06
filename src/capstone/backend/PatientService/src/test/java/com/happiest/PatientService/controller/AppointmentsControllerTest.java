package com.happiest.PatientService.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import com.happiest.PatientService.constants.Constants;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.exceptions.AppointmentBookingException;
import com.happiest.PatientService.exceptions.ResourceNotFoundException;
import com.happiest.PatientService.model.Appointments;
import com.happiest.PatientService.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class AppointmentsControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentsController appointmentsController;

    private Appointments appointment;
    private Doctors doctor;
    private Patients patient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointment = new Appointments();
        doctor = new Doctors();
        doctor.setDoctorId(1);
        patient = new Patients();
        patient.setPatientId(1);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.of(10, 0));
    }

//    @Test
//    void testGetAppointmentHistory_Success() {
//        when(appointmentService.getAppointmentHistory(anyInt())).thenReturn(Arrays.asList(appointment));
//
//        ResponseEntity<List<Appointments>> response = appointmentsController.getAppointmentHistory(1);
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(1, response.getBody().size());
//        verify(appointmentService, times(1)).getAppointmentHistory(anyInt());
//    }

    @Test
    void testGetAppointmentHistory_NotFound() {
        when(appointmentService.getAppointmentHistory(anyInt())).thenThrow(new ResourceNotFoundException("Patient not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentsController.getAppointmentHistory(1);
        });

        assertEquals("Patient not found", exception.getMessage());
        verify(appointmentService, times(1)).getAppointmentHistory(anyInt());
    }

    @Test
    void testCancelAppointment_Success() {
        doNothing().when(appointmentService).cancelAppointment(anyInt(), anyInt());

        ResponseEntity<Void> response = appointmentsController.cancelAppointment(1, 1);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).cancelAppointment(anyInt(), anyInt());
    }

    @Test
    void testCancelAppointment_NotFound() {
        doThrow(new ResourceNotFoundException("Appointment not found")).when(appointmentService).cancelAppointment(anyInt(), anyInt());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentsController.cancelAppointment(1, 1);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(appointmentService, times(1)).cancelAppointment(anyInt(), anyInt());
    }

    @Test
    void testGetUpcomingAppointments_Success() {
        when(appointmentService.getPatientUpcomingAppointments(anyInt())).thenReturn(Arrays.asList(appointment));

        ResponseEntity<List<Appointments>> response = appointmentsController.getUpcomingAppointments(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getPatientUpcomingAppointments(anyInt());
    }

    @Test
    void testGetUpcomingAppointments_NotFound() {
        when(appointmentService.getPatientUpcomingAppointments(anyInt())).thenThrow(new ResourceNotFoundException("Patient not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentsController.getUpcomingAppointments(1);
        });

        assertEquals("Patient not found", exception.getMessage());
        verify(appointmentService, times(1)).getPatientUpcomingAppointments(anyInt());
    }

    @Test
    void testGetAvailableDates_Success() {
        when(appointmentService.getAvailableDates(any(Doctors.class))).thenReturn(Arrays.asList(LocalDate.now()));

        ResponseEntity<List<LocalDate>> response = appointmentsController.getAvailableDates(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAvailableDates(any(Doctors.class));
    }

    @Test
    void testGetAvailableDates_NotFound() {
        when(appointmentService.getAvailableDates(any(Doctors.class))).thenThrow(new ResourceNotFoundException("Doctor not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentsController.getAvailableDates(1);
        });

        assertEquals("Doctor not found", exception.getMessage());
        verify(appointmentService, times(1)).getAvailableDates(any(Doctors.class));
    }

    @Test
    void testGetAvailableSlots_Success() {
        when(appointmentService.getAvailableSlots(any(Doctors.class), any(LocalDate.class))).thenReturn(Arrays.asList(LocalTime.now()));

        ResponseEntity<List<LocalTime>> response = appointmentsController.getAvailableSlots(1, LocalDate.now().toString());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAvailableSlots(any(Doctors.class), any(LocalDate.class));
    }

    @Test
    void testGetAvailableSlots_NotFound() {
        when(appointmentService.getAvailableSlots(any(Doctors.class), any(LocalDate.class))).thenThrow(new ResourceNotFoundException("Doctor not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentsController.getAvailableSlots(1, LocalDate.now().toString());
        });

        assertEquals("Doctor not found", exception.getMessage());
        verify(appointmentService, times(1)).getAvailableSlots(any(Doctors.class), any(LocalDate.class));
    }

    @Test
    void testBookAppointment_Success() {
        doNothing().when(appointmentService).bookAppointment(any(Appointments.class));

        ResponseEntity<String> response = appointmentsController.bookAppointment(appointment);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constants.APPOINTMENT_BOOKED_SUCCESSFULLY, response.getBody());
        verify(appointmentService, times(1)).bookAppointment(any(Appointments.class));
    }

    @Test
    void testBookAppointment_Failure() {
        doThrow(new AppointmentBookingException("Failed to book appointment")).when(appointmentService).bookAppointment(any(Appointments.class));

        AppointmentBookingException exception = assertThrows(AppointmentBookingException.class, () -> {
            appointmentsController.bookAppointment(appointment);
        });

        assertEquals("Failed to book appointment", exception.getMessage());
        verify(appointmentService, times(1)).bookAppointment(any(Appointments.class));
    }
}
