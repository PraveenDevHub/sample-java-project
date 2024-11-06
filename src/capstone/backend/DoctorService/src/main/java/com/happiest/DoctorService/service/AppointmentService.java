package com.happiest.DoctorService.service;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.dto.Patients;

import com.happiest.DoctorService.exception.AppointmentAlreadyCancelledException;
import com.happiest.DoctorService.exception.EmailException;
import com.happiest.DoctorService.exception.ResourceNotFoundException;
import com.happiest.DoctorService.exception.ValidationException;
import com.happiest.DoctorService.model.Appointments;
import com.happiest.DoctorService.repository.AppointmentRepository;
import com.happiest.DoctorService.repository.DoctorProfileRepository;
import com.happiest.DoctorService.repository.DoctorRepo;
import com.happiest.DoctorService.repository.PatientRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    public Appointments createAppointment(Appointments appointment) {

        Appointments savedAppointment = appointmentRepository.save(appointment);
        savedAppointment.setDoctor(doctorRepo.findById(appointment.getDoctor().getDoctorId()).get());
        savedAppointment.setPatient(patientRepo.findById(appointment.getPatient().getPatientId()).get());
        return savedAppointment;

    }

    //fetching all Appointments
    public List<Appointments> getAllAppointments() {
        List<Appointments> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
        }
        return appointments;
    }

    // Fetch upcoming appointments for a doctor
    public List<Appointments> getUpcomingAppointments(int doctorId) {
        Doctors doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND));

        // Fetch appointments with the specified status and sort by date and time
        return appointmentRepository.findByDoctorAndStatus(doctor, Appointments.AppointmentStatus.Scheduled,
                Sort.by(Sort.Order.asc("appointmentDate"), Sort.Order.asc("appointmentTimeSlot")));
    }

    public ResponseEntity<List<Appointments>> getAppointmentHistory(int doctorId) {
        List<Appointments> appointmentHistory = appointmentRepository.findAppointmentHistoryByDoctorId(doctorId);
        if (appointmentHistory.isEmpty()) {
            throw new ResourceNotFoundException(Constants.NO_PAST_APPOINTMENTS + doctorId);
        }
        return ResponseEntity.ok(appointmentHistory);
    }

    // Cancel an appointment
    public void cancelAppointment(int appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND + appointmentId));

        if (appointment.getStatus() == Appointments.AppointmentStatus.Cancelled) {
            throw new AppointmentAlreadyCancelledException(String.format(Constants.APPOINTMENT_ALREADY_CANCELLED, appointmentId));
        }
        // Cancel appointments
        appointment.setStatus(Appointments.AppointmentStatus.Cancelled);
        appointmentRepository.save(appointment);

        // Send email to the patient
        Patients patient = appointment.getPatient();
        String appointmentDetails = "Appointment ID: " + appointmentId + "\n" +
                "Doctor: " + appointment.getDoctor().getUser().getName() + "\n" +
                "Date: " + appointment.getAppointmentDate() + "\n" +
                "Time: " + appointment.getAppointmentTimeSlot();

        // Assuming the patient object has an email field it's nested inside the user object.
        String patientEmail = patient.getUser().getEmail(); // Adjust this based on your model

        emailService.sendCancellationEmail(patientEmail, patient.getUser().getName(), appointmentDetails);
    }

    // Mark an appointment as completed
    public void completeAppointment(int appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND + appointmentId));

        if (appointment.getStatus() == Appointments.AppointmentStatus.Cancelled) {
            throw new ValidationException(String.format(Constants.CANNOT_COMPLETE_CANCELLED_APPOINTMENT, appointmentId));
        }

        appointment.setStatus(Appointments.AppointmentStatus.Completed);
        appointmentRepository.save(appointment);
    }

    // Mark past appointments as completed
    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void markPastAppointmentsAsCompleted() {
        appointmentRepository.markPastAppointmentsAsCompleted(); // Call the native query
    }

    @Transactional
    public void cancelAllAppointments(Integer doctorId, LocalDate date) {
        List<Appointments> appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        for (Appointments appointment : appointments) {
            if (appointment.getStatus() == Appointments.AppointmentStatus.Scheduled) {
                appointment.setStatus(Appointments.AppointmentStatus.Cancelled);
                appointmentRepository.save(appointment);

                Patients patient = appointment.getPatient();
                String appointmentDetails = "Appointment ID: " + appointment.getAppointmentId() + "\n" +
                        "Doctor: " + appointment.getDoctor().getUser().getName() + "\n" +
                        "Date: " + appointment.getAppointmentDate() + "\n" +
                        "Time: " + appointment.getAppointmentTimeSlot();

                String patientEmail = patient.getUser().getEmail();
                try {
                    emailService.sendCancellationEmail(patientEmail, patient.getUser().getName(), appointmentDetails);
                } catch (Exception e) {
                    throw new EmailException(Constants.FAILED_TO_SEND_MAIL + patientEmail, e);
                }
            }
        }
        doctorProfileRepository.deleteByDoctorIdAndAvailableDate(doctorId, date);
    }
}



