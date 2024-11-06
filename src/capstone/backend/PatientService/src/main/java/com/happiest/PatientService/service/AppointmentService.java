package com.happiest.PatientService.service;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final Logger logger = LogManager.getLogger(AppointmentService.class.getName());

    // Fetch all appointments
    public List<Appointments> getAllAppointments() {
        logger.info(Constants.FETCHING_ALL_APPOINTMENTS);
        return appointmentRepository.findAll();
    }

    // Get upcoming appointments for a specific patient
    public List<Appointments> getPatientUpcomingAppointments(int patientId) {
        logger.info(Constants.FETCHING_UPCOMING_APPOINTMENTS, patientId);

        List<Appointments> upcomingAppointments = appointmentRepository.findUpcomingAppointments(patientId, LocalDate.now());
        if (upcomingAppointments.isEmpty()) {
            logger.warn(Constants.NO_UPCOMING_APPOINTMENTS_FOUND, patientId);
            throw new ResourceNotFoundException(Constants.NO_UPCOMING_APPOINTMENTS_FOUND + patientId);
        }
        return upcomingAppointments;
    }

    // Fetching appointment history for a patient
    public List<Appointments> getAppointmentHistory(int patientId) {
        logger.info(Constants.FETCHING_APPOINTMENT_HISTORY, patientId);

        List<Appointments> appointmentHistory = appointmentRepository.findByPatientIdAndStatus(
                patientId,
                Arrays.asList(Appointments.AppointmentStatus.Cancelled, Appointments.AppointmentStatus.Completed)
        );
        if (appointmentHistory.isEmpty()) {
            logger.warn(Constants.NO_APPOINTMENT_HISTORY_FOUND, patientId);
            throw new ResourceNotFoundException(Constants.NO_APPOINTMENT_HISTORY_FOUND + patientId);
        }
        return appointmentHistory;
    }

    // Cancel a specific appointment by patient
    @Transactional
    public void cancelAppointment(int appointmentId, int patientId) {
        logger.info(Constants.ATTEMPT_CANCEL_APPOINTMENT, appointmentId, patientId);

        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(Constants.APPOINTMENT_NOT_FOUND + appointmentId));

        if (appointment.getPatient().getPatientId() != patientId) {
            throw new UnauthorizedAccessException(Constants.UNAUTHORIZED_PATIENT_ACCESS);
        }

        if (appointment.getStatus() == Appointments.AppointmentStatus.Cancelled) {
            throw new AppointmentAlreadyCanceledException(Constants.APPOINTMENT_ALREADY_CANCELED);
        }

        appointment.setStatus(Appointments.AppointmentStatus.Cancelled);
        appointmentRepository.save(appointment);

        emailService.sendAppointmentCancellationEmailToDoctor(appointment);
        doctorProfileRepository.addTimeSlot(appointment.getDoctor().getDoctorId(),
                appointment.getAppointmentDate().toString(), appointment.getAppointmentTimeSlot().toString());

        logger.info(Constants.APPOINTMENT_CANCEL_SUCCESS, appointmentId);
    }

    // Fetch available dates for a specific doctor
    public List<LocalDate> getAvailableDates(Doctors doctor) {
        logger.info(Constants.FETCH_AVAILABLE_DATES, doctor.getDoctorId());

        List<LocalDate> availableDates = doctorProfileRepository.findByDoctor(doctor).stream()
                .map(DoctorProfile::getAvailableDate)
                .distinct()
                .collect(Collectors.toList());

        if (availableDates.isEmpty()) {
            throw new ResourceNotFoundException(Constants.NO_AVAILABLE_DATES + doctor.getDoctorId());
        }

        return availableDates;
    }

    // Fetch available slots for a doctor on a specific date
    public List<LocalTime> getAvailableSlots(Doctors doctor, LocalDate date) {
        LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
        LocalTime currentTime = LocalTime.now(ZoneId.systemDefault());

        List<LocalTime> allSlots = doctorProfileRepository.findByDoctorAndAvailableDate(doctor, date).stream()
                .flatMap(profile -> profile.getAvailableTimeSlotsList().stream())
                .filter(slot -> {
                    if (date.equals(currentDate)) {
                        return slot.isAfter(currentTime);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (allSlots.isEmpty()) {
            throw new ResourceNotFoundException(Constants.NO_AVAILABLE_SLOTS + doctor.getDoctorId() + " on " + date);
        }

        logger.info(Constants.AVAILABLE_SLOTS_MESSAGE, allSlots);

        return allSlots;
    }

    // Book an appointment
    @Transactional
    public void bookAppointment(Appointments appointment)  {
        logger.info(Constants.BOOKING_APPOINTMENT, appointment.getPatient().getPatientId(), appointment.getDoctor().getDoctorId());

        try {
            appointmentRepository.save(appointment);

            String formattedTimeSlot = appointment.getAppointmentTimeSlot().toString().substring(0, 5);

            doctorProfileRepository.removeTimeSlot(
                    appointment.getDoctor().getDoctorId(),
                    appointment.getAppointmentDate().toString(),
                    formattedTimeSlot
            );

            Doctors doctor = getDoctorById(appointment.getDoctor().getDoctorId());
            Patients patient = getPatientById(appointment.getPatient().getPatientId());

            emailService.sendBookingEmails(
                    doctor.getUser().getEmail(),
                    patient.getUser().getEmail(),
                    patient.getUser().getName(),
                    doctor.getUser().getName(),
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTimeSlot()
            );

            logger.info(Constants.APPOINTMENT_BOOKED_SUCCESS, appointment.getPatient().getPatientId(), appointment.getDoctor().getDoctorId());
        } catch (EmailException e) {
            logger.error(Constants.APPOINTMENT_BOOKING_FAILED, appointment.getPatient().getPatientId(), appointment.getDoctor().getDoctorId(), e);
            throw new AppointmentBookingException(Constants.APPOINTMENT_BOOKING_FAILED + appointment.getPatient().getPatientId() + " with doctorId: " + appointment.getDoctor().getDoctorId(),e);
        }
    }



    // Helper method to get doctor by ID
    public Doctors getDoctorById(Integer doctorId){
        try {
            return doctorRepo.findById(doctorId)
                    .orElseThrow(() -> new DoctorNotFoundException(Constants.DOCTOR_NOT_FOUND + doctorId));
        } catch (DoctorNotFoundException e) {
            throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
        }
    }

    // Helper method to get patient by ID
    public Patients getPatientById(Integer patientId) {
        try {
            return patientRepo.findById(patientId)
                    .orElseThrow(() -> new PatientNotFoundException(Constants.PATIENT_NOT_FOUND + patientId));
        } catch (PatientNotFoundException e) {
            throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
        }
    }
}

