package com.happiest.DoctorService.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.dto.Patients;
import com.happiest.DoctorService.model.Appointments;
import com.happiest.DoctorService.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@ExtendWith(MockitoExtension.class)
public class AppointmentsControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentsController appointmentsController;

    private Appointments appointment;
    private List<Appointments> appointmentsList;

    @BeforeEach
    void setUp() {
        appointment = new Appointments();
        appointment.setAppointmentId(1);
        appointment.setDoctor(new Doctors());
        appointment.setPatient(new Patients());
        appointment.setStatus(Appointments.AppointmentStatus.Scheduled);

        appointmentsList = Arrays.asList(appointment);
    }

    @Test
    void testCreateAppointmentSuccess() {
        when(appointmentService.createAppointment(any(Appointments.class))).thenReturn(appointment);

        ResponseEntity<Appointments> response = appointmentsController.createAppointment(appointment);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointment, response.getBody());
        verify(appointmentService, times(1)).createAppointment(any(Appointments.class));
    }

    @Test
    void testGetUpcomingAppointmentsSuccess() {
        when(appointmentService.getUpcomingAppointments(anyInt())).thenReturn(appointmentsList);

        ResponseEntity<List<Appointments>> response = appointmentsController.getUpcomingAppointments(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentsList, response.getBody());
        verify(appointmentService, times(1)).getUpcomingAppointments(anyInt());
    }

    @Test
    void testCancelAppointmentSuccess() {
        doNothing().when(appointmentService).cancelAppointment(anyInt());

        ResponseEntity<Void> response = appointmentsController.cancelAppointment(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).cancelAppointment(anyInt());
    }

    @Test
    void testGetDoctorAppointmentHistorySuccess() {
        when(appointmentService.getAppointmentHistory(anyInt())).thenReturn(ResponseEntity.ok(appointmentsList));

        ResponseEntity<List<Appointments>> response = appointmentsController.getDoctorAppointmentHistory(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(appointmentsList, response.getBody());
        verify(appointmentService, times(1)).getAppointmentHistory(anyInt());
    }

    @Test
    void testCompleteAppointmentSuccess() {
        doNothing().when(appointmentService).completeAppointment(anyInt());

        ResponseEntity<Void> response = appointmentsController.completeAppointment(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService, times(1)).completeAppointment(anyInt());
    }

    @Test
    void testCancelAllAppointmentsSuccess() {
        Map<String, String> request = new HashMap<>();
        request.put("date", "2024-10-29");

        doNothing().when(appointmentService).cancelAllAppointments(anyInt(), any(LocalDate.class));

        ResponseEntity<String> response = appointmentsController.cancelAllAppointments(1, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Constants.APPOINTMENTS_CANCELLED, response.getBody());
        verify(appointmentService, times(1)).cancelAllAppointments(anyInt(), any(LocalDate.class));
    }

    @Test
    void testCancelAllAppointmentsBadRequest() {
        Map<String, String> request = new HashMap<>();

        ResponseEntity<String> response = appointmentsController.cancelAllAppointments(1, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Constants.DATE_REQUIRED, response.getBody());
        verify(appointmentService, never()).cancelAllAppointments(anyInt(), any(LocalDate.class));
    }
}

