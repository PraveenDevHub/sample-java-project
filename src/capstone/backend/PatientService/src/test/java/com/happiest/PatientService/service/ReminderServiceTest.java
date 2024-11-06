package com.happiest.PatientService.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.dto.Users;
import com.happiest.PatientService.exceptions.EmailException;
import com.happiest.PatientService.model.Appointments;
import com.happiest.PatientService.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReminderServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReminderService reminderService;

    private Appointments appointment;
    private Patients patient;
    private Users user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointment = new Appointments();
        patient = new Patients();
        user = new Users();
        user.setEmail("patient@example.com");
        user.setName("John Doe");
        patient.setUser(user);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDate.now().plusDays(1));
        appointment.setAppointmentTimeSlot(LocalTime.of(10, 0));
    }

    @Test
    void testSendDailyReminders_Success() {
        when(appointmentRepository.findByAppointmentDateAndStatus(any(LocalDate.class), any(Appointments.AppointmentStatus.class)))
                .thenReturn(Arrays.asList(appointment));

        assertDoesNotThrow(() -> reminderService.sendDailyReminders());

        verify(appointmentRepository, times(1)).findByAppointmentDateAndStatus(any(LocalDate.class), any(Appointments.AppointmentStatus.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendDailyReminders_Failure() {
        when(appointmentRepository.findByAppointmentDateAndStatus(any(LocalDate.class), any(Appointments.AppointmentStatus.class)))
                .thenReturn(Arrays.asList(appointment));
        doThrow(new RuntimeException("Email sending failed")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        EmailException exception = assertThrows(EmailException.class, () -> reminderService.sendDailyReminders());

        assertEquals("Failed to send daily reminder email to patient@example.com", exception.getMessage());
        verify(appointmentRepository, times(1)).findByAppointmentDateAndStatus(any(LocalDate.class), any(Appointments.AppointmentStatus.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendOneMinuteReminders_Success() {
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.now().plusMinutes(1).truncatedTo(ChronoUnit.MINUTES));
        when(appointmentRepository.findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class)))
                .thenReturn(Arrays.asList(appointment));

        assertDoesNotThrow(() -> reminderService.sendOneMinuteReminders());

        verify(appointmentRepository, times(1)).findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendOneMinuteReminders_Failure() {
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.now().plusMinutes(1).truncatedTo(ChronoUnit.MINUTES));
        when(appointmentRepository.findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class)))
                .thenReturn(Arrays.asList(appointment));
        doThrow(new RuntimeException("Email sending failed")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        EmailException exception = assertThrows(EmailException.class, () -> reminderService.sendOneMinuteReminders());

        assertEquals("Failed to send one-minute reminder email to patient@example.com", exception.getMessage());
        verify(appointmentRepository, times(1)).findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendOneHourReminders_Success() {
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS));
        when(appointmentRepository.findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class)))
                .thenReturn(Arrays.asList(appointment));

        assertDoesNotThrow(() -> reminderService.sendOneHourReminders());

        verify(appointmentRepository, times(1)).findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendOneHourReminders_Failure() {
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS));
        when(appointmentRepository.findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class)))
                .thenReturn(Arrays.asList(appointment));
        doThrow(new RuntimeException("Email sending failed")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        EmailException exception = assertThrows(EmailException.class, () -> reminderService.sendOneHourReminders());

        assertEquals("Failed to send one-hour reminder email to patient@example.com", exception.getMessage());
        verify(appointmentRepository, times(1)).findByAppointmentDateAndTimeSlotAndStatus(any(LocalDate.class), any(LocalTime.class), any(Appointments.AppointmentStatus.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}
