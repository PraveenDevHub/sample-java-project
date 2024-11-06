package com.happiest.PatientService.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.happiest.PatientService.constants.Constants;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.exceptions.*;
import com.happiest.PatientService.model.Appointments;
import com.happiest.PatientService.model.DoctorProfile;
import com.happiest.PatientService.repository.AppointmentRepository;
import com.happiest.PatientService.repository.DoctorProfileRepository;
import com.happiest.PatientService.repository.DoctorRepo;
import com.happiest.PatientService.repository.PatientRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private Appointments appointment;
    private Doctors doctor;
    private Patients patient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        appointment = new Appointments();
        doctor = new Doctors();
        doctor.setDoctorId(1); // Ensure doctorId is set
        patient = new Patients();
        patient.setPatientId(1); // Ensure patientId is set
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.of(18, 0)); // Ensure appointmentTimeSlot is set
    }

    @Test
    void testGetAllAppointments_Success() {
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment));

        List<Appointments> appointments = appointmentService.getAllAppointments();

        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void testGetPatientUpcomingAppointments_Success() {
        when(appointmentRepository.findUpcomingAppointments(anyInt(), any(LocalDate.class)))
                .thenReturn(Arrays.asList(appointment));

        List<Appointments> appointments = appointmentService.getPatientUpcomingAppointments(1);

        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        verify(appointmentRepository, times(1)).findUpcomingAppointments(anyInt(), any(LocalDate.class));
    }

    @Test
    void testGetPatientUpcomingAppointments_NotFound() {
        when(appointmentRepository.findUpcomingAppointments(anyInt(), any(LocalDate.class)))
                .thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getPatientUpcomingAppointments(1);
        });

        assertEquals(Constants.NO_UPCOMING_APPOINTMENTS_FOUND + 1, exception.getMessage());
        verify(appointmentRepository, times(1)).findUpcomingAppointments(anyInt(), any(LocalDate.class));
    }

    @Test
    void testGetAppointmentHistory_Success() {
        when(appointmentRepository.findByPatientIdAndStatus(anyInt(), anyList()))
                .thenReturn(Arrays.asList(appointment));

        List<Appointments> appointments = appointmentService.getAppointmentHistory(1);

        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        verify(appointmentRepository, times(1)).findByPatientIdAndStatus(anyInt(), anyList());
    }

    @Test
    void testGetAppointmentHistory_NotFound() {
        when(appointmentRepository.findByPatientIdAndStatus(anyInt(), anyList()))
                .thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAppointmentHistory(1);
        });

        assertEquals(Constants.NO_APPOINTMENT_HISTORY_FOUND + 1, exception.getMessage());
        verify(appointmentRepository, times(1)).findByPatientIdAndStatus(anyInt(), anyList());
    }

//    @Test
//    void testCancelAppointment_Success() {
//        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));
//        doNothing().when(emailService).sendAppointmentCancellationEmailToDoctor(any(Appointments.class));
//        doNothing().when(doctorProfileRepository).addTimeSlot(anyInt(), anyString(), anyString());
//
//        appointmentService.cancelAppointment(1, 1);
//
//        assertEquals(Appointments.AppointmentStatus.Cancelled, appointment.getStatus());
//        verify(appointmentRepository, times(1)).findById(anyInt());
//        verify(appointmentRepository, times(1)).save(any(Appointments.class));
//        verify(emailService, times(1)).sendAppointmentCancellationEmailToDoctor(any(Appointments.class));
//        verify(doctorProfileRepository, times(1)).addTimeSlot(anyInt(), anyString(), anyString());
//    }

    @Test
    void testCancelAppointment_NotFound() {
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.cancelAppointment(1, 1);
        });

        assertEquals(Constants.APPOINTMENT_NOT_FOUND + 1, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCancelAppointment_UnauthorizedAccess() {
        appointment.getPatient().setPatientId(2);
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(UnauthorizedAccessException.class, () -> {
            appointmentService.cancelAppointment(1, 1);
        });

        assertEquals(Constants.UNAUTHORIZED_PATIENT_ACCESS, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCancelAppointment_AlreadyCanceled() {
        appointment.setStatus(Appointments.AppointmentStatus.Cancelled);
        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(AppointmentAlreadyCanceledException.class, () -> {
            appointmentService.cancelAppointment(1, 1);
        });

        assertEquals(Constants.APPOINTMENT_ALREADY_CANCELED, exception.getMessage());
        verify(appointmentRepository, times(1)).findById(anyInt());
    }

    @Test
    void testGetAvailableDates_Success() {
        DoctorProfile doctorProfile = new DoctorProfile(1, doctor, LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 0), 30, "Available", Arrays.asList(LocalTime.of(9, 0), LocalTime.of(9, 30)));
        when(doctorProfileRepository.findByDoctor(any(Doctors.class)))
                .thenReturn(Arrays.asList(doctorProfile));

        List<LocalDate> dates = appointmentService.getAvailableDates(doctor);

        assertNotNull(dates);
        assertEquals(1, dates.size());
        verify(doctorProfileRepository, times(1)).findByDoctor(any(Doctors.class));
    }

    @Test
    void testGetAvailableDates_NotFound() {
        when(doctorProfileRepository.findByDoctor(any(Doctors.class)))
                .thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAvailableDates(doctor);
        });

        assertEquals(Constants.NO_AVAILABLE_DATES + doctor.getDoctorId(), exception.getMessage());
        verify(doctorProfileRepository, times(1)).findByDoctor(any(Doctors.class));
    }

//    @Test
//    void testGetAvailableSlots_Success() {
//        DoctorProfile doctorProfile = new DoctorProfile(1, doctor, LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(17, 0), 30, "Available", Arrays.asList(LocalTime.of(10, 0)));
//        when(doctorProfileRepository.findByDoctorAndAvailableDate(any(Doctors.class), any(LocalDate.class)))
//                .thenReturn(Arrays.asList(doctorProfile));
//
//        List<LocalTime> slots = appointmentService.getAvailableSlots(doctor, LocalDate.now());
//
//        assertNotNull(slots);
//        assertEquals(1, slots.size());
//        verify(doctorProfileRepository, times(1)).findByDoctorAndAvailableDate(any(Doctors.class), any(LocalDate.class));
//    }

    @Test
    void testGetAvailableSlots_NotFound() {
        when(doctorProfileRepository.findByDoctorAndAvailableDate(any(Doctors.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getAvailableSlots(doctor, LocalDate.now());
        });

        assertEquals(Constants.NO_AVAILABLE_SLOTS + doctor.getDoctorId() + " on " + LocalDate.now(), exception.getMessage());
        verify(doctorProfileRepository, times(1)).findByDoctorAndAvailableDate(any(Doctors.class), any(LocalDate.class));
    }
    

    @Test
    void testGetDoctorById_Success() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));

        Doctors foundDoctor = appointmentService.getDoctorById(1);

        assertNotNull(foundDoctor);
        assertEquals(doctor, foundDoctor);
        verify(doctorRepo, times(1)).findById(anyInt());
    }

    @Test
    void testGetDoctorById_NotFound() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getDoctorById(1);
        });

        assertEquals(Constants.DOCTOR_NOT_FOUND, exception.getMessage());
        verify(doctorRepo, times(1)).findById(anyInt());
    }

    @Test
    void testGetPatientById_Success() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient));

        Patients foundPatient = appointmentService.getPatientById(1);

        assertNotNull(foundPatient);
        assertEquals(patient, foundPatient);
        verify(patientRepo, times(1)).findById(anyInt());
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.getPatientById(1);
        });

        assertEquals(Constants.PATIENT_NOT_FOUND, exception.getMessage());
        verify(patientRepo, times(1)).findById(anyInt());
    }
}
