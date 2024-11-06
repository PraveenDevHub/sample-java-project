package com.happiest.DoctorService.controller;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.exception.AppointmentAlreadyCancelledException;
import com.happiest.DoctorService.exception.ResourceNotFoundException;
import com.happiest.DoctorService.exception.ValidationException;
import com.happiest.DoctorService.model.Appointments;
import com.happiest.DoctorService.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/appoi")
public class AppointmentsController {
    @Autowired
    AppointmentService appointmentService;

    private static final Logger logger = LogManager.getLogger(AppointmentsController.class);

    @PostMapping("/create")
    @Operation(summary = "Create a new appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "400", description = "Invalid appointment details")
    })
    public ResponseEntity<Appointments> createAppointment(@RequestBody Appointments appointment) {
        logger.info(Constants.CREATING_APPOINTMENT);
        Appointments createdAppointment = appointmentService.createAppointment(appointment);
        logger.info(Constants.APPOINTMENT_CREATED, createdAppointment.getDoctor().getDoctorId());
        return ResponseEntity.ok(createdAppointment);
    }


    // Fetch upcoming appointments for a specific doctor
    @GetMapping("/{doctorId}/appointments")
    @Operation(summary = "Fetch upcoming appointments for a specific doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND)
    })
    public ResponseEntity<List<Appointments>> getUpcomingAppointments(@PathVariable int doctorId) {
        logger.info(Constants.FETCHING_UPCOMING_APPOINTMENTS, doctorId);
        List<Appointments> appointments = appointmentService.getUpcomingAppointments(doctorId);
        logger.info(Constants.UPCOMING_APPOINTMENTS_RETRIEVED, doctorId);
        return ResponseEntity.ok(appointments);
    }

    // Cancel a specific appointment
    @PutMapping("/appointment/{appointmentId}/cancel")
    @Operation(summary = "Cancel a specific appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "400", description = "Appointment already cancelled")
    })
    public ResponseEntity<Void> cancelAppointment(@PathVariable int appointmentId) {
        logger.info(Constants.CANCELLING_APPOINTMENT, appointmentId);
        appointmentService.cancelAppointment(appointmentId);
        logger.info(Constants.APPOINTMENT_CANCELLED, appointmentId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/appointment/history/{doctorId}")
    @Operation(summary = "Fetch appointment history (cancelled and completed appointments) for a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND)
    })
    public ResponseEntity<List<Appointments>> getDoctorAppointmentHistory(@PathVariable int doctorId) {
        logger.info(Constants.FETCHING_APPOINTMENT_HISTORY, doctorId);
        List<Appointments> appointmentHistory = appointmentService.getAppointmentHistory(doctorId).getBody();
        logger.info(Constants.APPOINTMENT_HISTORY_RETRIEVED, doctorId);
        return ResponseEntity.ok(appointmentHistory);
    }

    // Mark an appointment as completed
    @PutMapping("/{appointmentId}/complete")
    @Operation(summary = "Mark an appointment as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid appointment details")
    })
    public ResponseEntity<Void> completeAppointment(@PathVariable int appointmentId) {
        logger.info(Constants.COMPLETING_APPOINTMENT, appointmentId);
        appointmentService.completeAppointment(appointmentId);
        logger.info(Constants.APPOINTMENT_COMPLETED, appointmentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{doctorId}/cancelAll")
    @Operation(summary = "Cancel all appointments for a specific doctor on a given date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.APPOINTMENTS_CANCELLED),
            @ApiResponse(responseCode = "400", description = Constants.DATE_REQUIRED),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND)
    })
    public ResponseEntity<String> cancelAllAppointments(@PathVariable Integer doctorId, @RequestBody Map<String, String> request) {
        String date = request.get("date");
        if (date != null) {
            logger.info(Constants.CANCELLING_ALL_APPOINTMENTS, doctorId, date);
            LocalDate appointmentDate = LocalDate.parse(date);
            appointmentService.cancelAllAppointments(doctorId, appointmentDate);
            logger.info(Constants.ALL_APPOINTMENTS_CANCELLED, doctorId, date);
            return ResponseEntity.ok(Constants.APPOINTMENTS_CANCELLED);
        } else {
            logger.warn(Constants.DATE_REQUIRED, doctorId);
            return ResponseEntity.badRequest().body(Constants.DATE_REQUIRED);
        }
    }

    // Mark past appointments as completed (optional endpoint for manual triggering)
//    @PutMapping("/complete-past")
//    public ResponseEntity<Void> markPastAppointmentsAsCompleted() {
//        appointmentService.markPastAppointmentsAsCompleted();
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }


}
