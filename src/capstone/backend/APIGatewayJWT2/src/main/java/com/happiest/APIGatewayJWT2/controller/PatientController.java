package com.happiest.APIGatewayJWT2.controller;

import com.happiest.APIGatewayJWT2.apigateway.PatientServiceInterface;
import com.happiest.APIGatewayJWT2.constants.LoggerConstants;
import com.happiest.APIGatewayJWT2.constants.PatientControllerConstants;
import com.happiest.APIGatewayJWT2.model.Appointments;
import com.happiest.APIGatewayJWT2.model.Doctors;
import com.happiest.APIGatewayJWT2.model.Patients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/patient")
public class PatientController {

    private static final Logger logger = LogManager.getLogger(PatientController.class);

    @Autowired
    private PatientServiceInterface patientServiceInterface;

    @GetMapping("/doctors")
    public List<Doctors> getDoctors(@RequestParam(required = false) Integer limit) {
        logger.info(LoggerConstants.FETCHING_DOCTORS, limit);
        return patientServiceInterface.getDoctors(limit);
    }

    @GetMapping("/doctors/filter")
    public ResponseEntity<List<Doctors>> filterDoctors(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String hospital,
            @RequestParam(required = false) String searchTerm) {
        logger.info(LoggerConstants.FILTERING_DOCTORS, state, city, specialization, hospital, searchTerm);
        return patientServiceInterface.filterDoctors(state, city, specialization, hospital, searchTerm);
    }


    @GetMapping("/{patientId}")
    public ResponseEntity<Patients> getPatientById(@PathVariable Integer patientId) {
        logger.info(LoggerConstants.FETCHING_PATIENT_BY_ID, patientId);
        return patientServiceInterface.getPatientById(patientId);
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointments> createAppointment(@RequestBody Appointments appointment) {
        logger.info(LoggerConstants.CREATING_APPOINTMENT, appointment.getPatient().getPatientId());
        return patientServiceInterface.createAppointment(appointment);
    }

    @GetMapping("/appointments/{patientId}")
    public ResponseEntity<List<Appointments>> getAppointmentsByPatientId(@PathVariable Integer patientId) {
        logger.info(LoggerConstants.FETCHING_APPOINTMENTS_BY_PATIENT_ID, patientId);
        return patientServiceInterface.getAppointmentsByPatientId(patientId);
    }

    @GetMapping("/appointment/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestParam Integer doctorId) {
        logger.info(LoggerConstants.FETCHING_AVAILABLE_DATES, doctorId);
        return patientServiceInterface.getAvailableDates(doctorId);
    }

    @GetMapping("/appointment/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(@RequestParam Integer doctorId, @RequestParam String date) {
        logger.info(LoggerConstants.FETCHING_AVAILABLE_SLOTS, doctorId, date);
        return patientServiceInterface.getAvailableSlots(doctorId, date);
    }

    @PostMapping("/appointment/bookappointment")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointments appointment) {
        logger.info(LoggerConstants.BOOKING_APPOINTMENT, appointment.getPatient().getPatientId());
        return patientServiceInterface.bookAppointment(appointment);
    }

    @GetMapping("/getPatientProfile/{patientId}")
    public ResponseEntity<Patients> getPatientProfile(@PathVariable Integer patientId) {
        logger.info(LoggerConstants.FETCHING_PATIENT_PROFILE, patientId);
        return patientServiceInterface.getPatientProfile(patientId);
    }

    @PutMapping("/updateProfile/{patientId}")
    public ResponseEntity<Patients> updatePatientProfile(@PathVariable Integer patientId, @RequestBody Patients updatedPatient) {
        logger.info(LoggerConstants.UPDATING_PATIENT_PROFILE, patientId);
        return patientServiceInterface.updatePatientProfile(patientId, updatedPatient);
    }
}
