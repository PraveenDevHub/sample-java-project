package com.happiest.DoctorService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.dto.Patients;
import com.happiest.DoctorService.dto.Users;
import com.happiest.DoctorService.exception.AppointmentAlreadyCancelledException;
import com.happiest.DoctorService.exception.EmailException;
import com.happiest.DoctorService.exception.ResourceNotFoundException;
import com.happiest.DoctorService.exception.ValidationException;
import com.happiest.DoctorService.model.Appointments;
import com.happiest.DoctorService.repository.AppointmentRepository;
import com.happiest.DoctorService.repository.DoctorProfileRepository;
import com.happiest.DoctorService.repository.DoctorRepo;
import com.happiest.DoctorService.repository.PatientRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private DoctorProfileRepository doctorProfileRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Doctors doctor;
    private Patients patient;
    private Users doctorUser;
    private Users patientUser;
    private Appointments appointment;

    @BeforeEach
    void setUp() {
        doctorUser = new Users();
        doctorUser.setName("Dr. John Doe");

        patientUser = new Users();
        patientUser.setName("Jane Doe");
        patientUser.setEmail("jane.doe@example.com");

        doctor = new Doctors();
        doctor.setDoctorId(1);
        doctor.setUser(doctorUser);

        patient = new Patients();
        patient.setPatientId(1);
        patient.setUser(patientUser);

        appointment = new Appointments();
        appointment.setAppointmentId(1);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(Appointments.AppointmentStatus.Scheduled);
    }


    @Test
    void testCreateAppointmentSuccess() {
        when(appointmentRepository.save(any(Appointments.class))).thenReturn(appointment);
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient));

        Appointments createdAppointment = appointmentService.createAppointment(appointment);

        assertNotNull(createdAppointment);
        assertEquals(1, createdAppointment.getAppointmentId());
        verify(appointmentRepository, times(1)).save(any(Appointments.class));
    }

    @Test
    void testGetAllAppointmentsSuccess() {
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment));

        List<Appointments> appointments = appointmentService.getAllAppointments();

        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllAppointmentsFailure() {
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAllAppointments();
        });

        assertEquals(Constants.APPOINTMENT_NOT_FOUND, exception.getMessage());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void testGetUpcomingAppointmentsSuccess() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorAndStatus(any(Doctors.class), any(Appointments.AppointmentStatus.class), any(Sort.class)))
                .thenReturn(Arrays.asList(appointment));

        List<Appointments> appointments = appointmentService.getUpcomingAppointments(1);

        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
        verify(doctorRepo, times(1)).findById(anyInt());
        verify(appointmentRepository, times(1)).findByDoctorAndStatus(any(Doctors.class), any(Appointments.AppointmentStatus.class), any(Sort.class));
    }

    @Test
    void testGetUpcomingAppointmentsFailure() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getUpcomingAppointments(1);
        });

        assertEquals(Constants.DOCTOR_NOT_FOUND, exception.getMessage());
        verify(doctorRepo, times(1)).findById(anyInt());
    }

    @Test
    void testGetAppointmentHistorySuccess() {
        when(appointmentRepository.findAppointmentHistoryByDoctorId(anyInt())).thenReturn(Arrays.asList(appointment));

        ResponseEntity<List<Appointments>> response = appointmentService.getAppointmentHistory(1);

        assertNotNull(response);
        assertFalse(response.getBody().isEmpty());
        verify(appointmentRepository, times(1)).findAppointmentHistoryByDoctorId(anyInt());
    }

    @Test
    void testGetAppointmentHistoryFailure() {
        when(appointmentRepository.findAppointmentHistoryByDoctorId(anyInt())).thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAppointmentHistory(1);
        });

        assertEquals(Constants.NO_PAST_APPOINTMENTS + 1, exception.getMessage());
        verify(appointmentRepository, times(1)).findAppointmentHistoryByDoctorId(anyInt());
    }

    @Test
    void testCancelAppointmentSuccess() {
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(1);

        assertEquals(Appointments.AppointmentStatus.Cancelled, appointment.getStatus());
        verify(appointmentRepository, times(1)).findById(anyInt());
        verify(appointmentRepository, times(1)).save(any(Appointments.class));
        verify(emailService, times(1)).sendCancellationEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testCancelAppointmentAlreadyCancelled() {
        appointment.setStatus(Appointments.AppointmentStatus.Cancelled);
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(AppointmentAlreadyCancelledException.class, () -> {
            appointmentService.cancelAppointment(1);
        });

        assertEquals(String.format(Constants.APPOINTMENT_ALREADY_CANCELLED, 1), exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCancelAppointmentNotFound() {
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.cancelAppointment(1);
        });

        assertEquals(Constants.APPOINTMENT_NOT_FOUND + 1, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCompleteAppointmentSuccess() {
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));

        appointmentService.completeAppointment(1);

        assertEquals(Appointments.AppointmentStatus.Completed, appointment.getStatus());
        verify(appointmentRepository, times(1)).findById(anyInt());
        verify(appointmentRepository, times(1)).save(any(Appointments.class));
    }

    @Test
    void testCompleteAppointmentCancelled() {
        appointment.setStatus(Appointments.AppointmentStatus.Cancelled);
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(ValidationException.class, () -> {
            appointmentService.completeAppointment(1);
        });

        assertEquals(String.format(Constants.CANNOT_COMPLETE_CANCELLED_APPOINTMENT, 1), exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCompleteAppointmentNotFound() {
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.completeAppointment(1);
        });

        assertEquals(Constants.APPOINTMENT_NOT_FOUND + 1, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCancelAllAppointmentsSuccess() {
        when(appointmentRepository.findByDoctorIdAndAppointmentDate(anyInt(), any(LocalDate.class)))
                .thenReturn(Arrays.asList(appointment));

        appointmentService.cancelAllAppointments(1, LocalDate.now());

        assertEquals(Appointments.AppointmentStatus.Cancelled, appointment.getStatus());
        verify(appointmentRepository, times(1)).findByDoctorIdAndAppointmentDate(anyInt(), any(LocalDate.class));
        verify(appointmentRepository, times(1)).save(any(Appointments.class));
        verify(emailService, times(1)).sendCancellationEmail(anyString(), anyString(), anyString());
        verify(doctorProfileRepository, times(1)).deleteByDoctorIdAndAvailableDate(anyInt(), any(LocalDate.class));
    }

    @Test
    void testCancelAllAppointmentsEmailFailure() {
        when(appointmentRepository.findByDoctorIdAndAppointmentDate(anyInt(), any(LocalDate.class)))
                .thenReturn(Arrays.asList(appointment));
        doThrow(new RuntimeException("Email service failure")).when(emailService).sendCancellationEmail(anyString(), anyString(), anyString());

        Exception exception = assertThrows(EmailException.class, () -> {
            appointmentService.cancelAllAppointments(1, LocalDate.now());
        });

        assertTrue(exception.getMessage().contains(Constants.FAILED_TO_SEND_MAIL));
        verify(appointmentRepository, times(1)).findByDoctorIdAndAppointmentDate(anyInt(), any(LocalDate.class));
        verify(appointmentRepository, times(1)).save(any(Appointments.class));
        verify(emailService, times(1)).sendCancellationEmail(anyString(), anyString(), anyString());
        verify(doctorProfileRepository, never()).deleteByDoctorIdAndAvailableDate(anyInt(), any(LocalDate.class));
    }

}
