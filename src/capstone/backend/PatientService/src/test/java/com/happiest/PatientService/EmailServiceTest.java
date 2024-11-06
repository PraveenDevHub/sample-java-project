package com.happiest.PatientService;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.dto.Users;
import com.happiest.PatientService.model.Appointments;
import com.happiest.PatientService.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private Appointments appointment;
    private Doctors doctor;
    private Patients patient;
    private Users doctorUser;
    private Users patientUser;

    @BeforeEach
    void setUp() {
        doctorUser = new Users();
        doctorUser.setEmail("doctor@example.com");
        doctorUser.setName("Dr. Smith");

        patientUser = new Users();
        patientUser.setEmail("patient@example.com");
        patientUser.setName("John Doe");

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
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTimeSlot(LocalTime.now().plusHours(1));
    }

    @Test
    void testSendAppointmentCancellationEmailToDoctor_Success() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendAppointmentCancellationEmailToDoctor(appointment);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendAppointmentCancellationEmailToDoctor_Failure() {
        doThrow(new RuntimeException("Email sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendAppointmentCancellationEmailToDoctor(appointment);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendReminderEmailToPatient_Success() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendReminderEmailToPatient(appointment);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendReminderEmailToPatient_Failure() {
        doThrow(new RuntimeException("Email sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendReminderEmailToPatient(appointment);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendBookingEmails_Success() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendBookingEmails(
                "doctor@example.com",
                "patient@example.com",
                "John Doe",
                "Dr. Smith",
                LocalDate.now(),
                LocalTime.now().plusHours(1)
        );

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendBookingEmails_Failure() {
        doThrow(new RuntimeException("Email sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendBookingEmails(
                "doctor@example.com",
                "patient@example.com",
                "John Doe",
                "Dr. Smith",
                LocalDate.now(),
                LocalTime.now().plusHours(1)
        );

        verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_Success() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail("test@example.com", "Test Subject", "Test Body");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmail_Failure() {
        doThrow(new RuntimeException("Email sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail("test@example.com", "Test Subject", "Test Body");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

