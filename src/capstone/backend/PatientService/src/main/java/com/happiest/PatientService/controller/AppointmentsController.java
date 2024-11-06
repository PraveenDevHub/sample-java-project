package com.happiest.PatientService.controller;

import com.happiest.PatientService.constants.Constants;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.model.Appointments;
import com.happiest.PatientService.service.AppointmentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/appoi")
public class AppointmentsController {

    private static final Logger logger = LogManager.getLogger(AppointmentsController.class.getName());
    @Autowired
    AppointmentService appointmentService;


    @GetMapping("/appointment/history/{patientId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = Constants.PATIENT_NOT_FOUND)
    })
    public ResponseEntity<List<Appointments>> getAppointmentHistory(@PathVariable int patientId) {
        logger.info(Constants.FETCHING_APPOINTMENT_HISTORY, patientId);
        List<Appointments> appointmentHistory = appointmentService.getAppointmentHistory(patientId);
        logger.info(Constants.SUCCESSFUL_APPOINTMENT_HISTORY_RETRIEVAL, patientId);
        return ResponseEntity.ok(appointmentHistory);
    }

    @PutMapping("appointment/cancel/{appointmentId}/{patientId}")
    @Operation(summary = "Cancel an appointment for a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_CANCELLATION),
            @ApiResponse(responseCode = "404", description = Constants.APPOINTMENT_NOT_FOUND),
            @ApiResponse(responseCode = "400", description = Constants.FAILED_BOOKING)
    })
    public ResponseEntity<Void> cancelAppointment(@PathVariable int appointmentId, @PathVariable int patientId) {
        logger.info(Constants.CANCELING_APPOINTMENT, appointmentId, patientId);
        appointmentService.cancelAppointment(appointmentId, patientId);
        logger.info(Constants.SUCCESSFUL_APPOINTMENT_CANCELLATION, appointmentId, patientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/appointment/upcoming/{patientId}")
    @Operation(summary = "Show upcoming appointments for patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = Constants.PATIENT_NOT_FOUND)
    })
    public ResponseEntity<List<Appointments>> getUpcomingAppointments(@PathVariable int patientId) {
        logger.info(Constants.LOG_FETCH_UPCOMING_APPOINTMENTS, patientId);
        List<Appointments> upcomingAppointments = appointmentService.getPatientUpcomingAppointments(patientId);
        logger.info(Constants.LOG_UPCOMING_APPOINTMENTS_SUCCESS, patientId);
        return ResponseEntity.ok(upcomingAppointments);
    }

    @Operation(summary = "Get Available Dates for a Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.AVAILABLE_DATES_RETRIEVED),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestParam Integer doctorId) {
        logger.info(Constants.LOG_FETCH_AVAILABLE_DATES, doctorId);
        Doctors doctor = new Doctors();
        doctor.setDoctorId(doctorId);
        List<LocalDate> availableDates = appointmentService.getAvailableDates(doctor);
        logger.info(Constants.LOG_AVAILABLE_DATES_SUCCESS, doctorId);
        return ResponseEntity.ok(availableDates);
    }

    @Operation(summary = "Get Available Appointment Slots for a Patient on a Specific Date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.AVAILABLE_SLOTS_RETRIEVED),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND),
            @ApiResponse(responseCode = "400", description = Constants.INVALID_DATE_FORMAT),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam Integer doctorId,
            @RequestParam String date) {

        logger.info(Constants.LOG_FETCH_AVAILABLE_SLOTS, doctorId, date);
        Doctors doctor = new Doctors();
        doctor.setDoctorId(doctorId);

        LocalDate localDate = LocalDate.parse(date.trim());
        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(doctor, localDate);

        logger.info(Constants.LOG_AVAILABLE_SLOTS_SUCCESS, doctorId, date);
        return ResponseEntity.ok(availableSlots);
    }

    @Operation(summary = "Book a new appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.APPOINTMENT_BOOKED_SUCCESSFULLY),
            @ApiResponse(responseCode = "400", description = Constants.FAILED_TO_BOOK_APPOINTMENT),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointments appointment) {
        logger.info(Constants.LOG_BOOK_APPOINTMENT, appointment.getPatient().getPatientId());
        appointmentService.bookAppointment(appointment);
        logger.info(Constants.LOG_APPOINTMENT_BOOKED_SUCCESS, appointment.getPatient().getPatientId());
        return ResponseEntity.ok(Constants.APPOINTMENT_BOOKED_SUCCESSFULLY);
    }
}
