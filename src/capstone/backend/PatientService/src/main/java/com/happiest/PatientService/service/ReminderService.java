package com.happiest.PatientService.service;

import com.happiest.PatientService.exceptions.EmailException;
import com.happiest.PatientService.model.Appointments;
import com.happiest.PatientService.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderService {

    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 9 * * ?") // Executes every day at 9 AM
    public void sendDailyReminders() throws EmailException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        logger.info("Sending daily reminders for appointments on {}", tomorrow);

        List<Appointments> dayBeforeAppointments = appointmentRepository
                .findByAppointmentDateAndStatus(tomorrow, Appointments.AppointmentStatus.Scheduled);

        for (Appointments appointment : dayBeforeAppointments) {
            String patientEmail = appointment.getPatient().getUser().getEmail();
            try {
                emailService.sendEmail(
                        patientEmail,
                        "Appointment Reminder - 1 Day Left",
                        "Dear " + appointment.getPatient().getUser().getName() +
                                ",\nThis is a reminder that you have an appointment scheduled for tomorrow at " +
                                appointment.getAppointmentTimeSlot() + "."
                );
                logger.info("Sent reminder email to {} for appointment on {}", patientEmail, appointment.getAppointmentDate());
            } catch (Exception e) {
                logger.error("Failed to send daily reminder email to {}: {}", patientEmail, e.getMessage());
                throw new EmailException("Failed to send daily reminder email to " + patientEmail, e);
            }
        }
    }

    // Similarly, update the other methods to throw EmailException
    @Scheduled(cron = "0 * * * * ?") // Executes every minute
    public void sendOneMinuteReminders() throws EmailException {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES); // Truncate seconds
        LocalDate today = LocalDate.now();

        List<Appointments> minuteBeforeAppointments = appointmentRepository
                .findByAppointmentDateAndTimeSlotAndStatus(today, now.plusMinutes(1), Appointments.AppointmentStatus.Scheduled);

        for (Appointments appointment : minuteBeforeAppointments) {
            String patientEmail = appointment.getPatient().getUser().getEmail();
            try {
                emailService.sendEmail(
                        patientEmail,
                        "Appointment Reminder - 1 Minute Left",
                        "Dear " + appointment.getPatient().getUser().getName() +
                                ",\nThis is a reminder that your appointment is in one minute at " +
                                appointment.getAppointmentTimeSlot() + "."
                );
            } catch (Exception e) {
                throw new EmailException("Failed to send one-minute reminder email to " + patientEmail, e);
            }
        }
    }

    @Scheduled(cron = "0 0 * * * ?") // Executes every hour
    public void sendOneHourReminders() throws EmailException {
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.HOURS); // Truncate seconds
        LocalDate today = LocalDate.now();

        List<Appointments> hourBeforeAppointments = appointmentRepository
                .findByAppointmentDateAndTimeSlotAndStatus(today, now.plusHours(1), Appointments.AppointmentStatus.Scheduled);

        for (Appointments appointment : hourBeforeAppointments) {
            String patientEmail = appointment.getPatient().getUser().getEmail();
            try {
                emailService.sendEmail(
                        patientEmail,
                        "Appointment Reminder - 1 Hour Left",
                        "Dear " + appointment.getPatient().getUser().getName() +
                                ",\nThis is a reminder that your appointment is in one hour at " +
                                appointment.getAppointmentTimeSlot() + "."
                );
            } catch (Exception e) {
                throw new EmailException("Failed to send one-hour reminder email to " + patientEmail, e);
            }
        }
    }
}

