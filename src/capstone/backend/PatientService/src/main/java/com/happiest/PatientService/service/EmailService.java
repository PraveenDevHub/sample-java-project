package com.happiest.PatientService.service;

import com.happiest.PatientService.constants.EmailConstants;
import com.happiest.PatientService.model.Appointments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LogManager.getLogger(EmailService.class.getName());

    public void sendAppointmentCancellationEmailToDoctor(Appointments appointment) {
        String doctorEmail = appointment.getDoctor().getUser().getEmail();
        String subject = (EmailConstants.CANCELLATION_SUBJECT);
        String body = String.format(EmailConstants.CANCELLATION_BODY,
                appointment.getDoctor().getUser().getName(),
                appointment.getPatient().getUser().getName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTimeSlot());

        logger.info("Sending cancellation email to doctor at: {}", doctorEmail);
        sendEmail(doctorEmail, subject, body);
    }

    // Send reminder email to patient
    public void sendReminderEmailToPatient(Appointments appointment) {
        String patientEmail = appointment.getPatient().getUser().getEmail();
        String subject = (EmailConstants.REMINDER_SUBJECT);
        String body = String.format(EmailConstants.REMINDER_BODY,
                appointment.getPatient().getUser().getName(),
                appointment.getDoctor().getUser().getName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTimeSlot());

        logger.info("Sending reminder email to patient at: {}", patientEmail);
        sendEmail(patientEmail, subject, body);
    }

    // Send booking emails to both doctor and patient
    public void sendBookingEmails(String doctorEmail, String patientEmail, String patientName, String doctorName, LocalDate appointmentDate, LocalTime appointmentTime) {
        // Email to Doctor
        String doctorSubject = (EmailConstants.NEW_BOOKING_SUBJECT_DOCTOR);
        String doctorBody = String.format (EmailConstants.BOOKING_BODY_DOCTOR,
                doctorName, patientName, appointmentDate, appointmentTime);

        logger.info("Sending booking confirmation email to doctor at: {}", doctorEmail);
        sendEmail(doctorEmail, doctorSubject, doctorBody);

        // Email to Patient
        String patientSubject = (EmailConstants.NEW_BOOKING_SUBJECT_PATIENT);
        String patientBody = String.format(EmailConstants.BOOKING_BODY_PATIENT,
                patientName, doctorName, appointmentDate, appointmentTime);

        logger.info("Sending booking confirmation email to patient at: {}", patientEmail);
        sendEmail(patientEmail, patientSubject, patientBody);
    }

    // Generic email sending method
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            logger.info("Sending email to: {} with subject: {}", to, subject);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", to, e);
        }
    }
}

