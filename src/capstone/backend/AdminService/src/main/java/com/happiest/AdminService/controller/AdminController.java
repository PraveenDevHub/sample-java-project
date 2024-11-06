package com.happiest.AdminService.controller;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.repository.AppointmentRepository;
import com.happiest.AdminService.repository.DoctorRepo;
import com.happiest.AdminService.repository.PatientRepo;
import com.happiest.AdminService.repository.UserRepo;
import com.happiest.AdminService.service.AdminService;
import com.happiest.AdminService.service.DoctorService;

//import io.swagger.annotations.Api;
import com.happiest.AdminService.utility.RBundle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import org.springframework.web.bind.annotation.*;
//import org.springdoc.api.annotations.Operation;
//import org.springdoc.api.annotations.responses.ApiResponse;
//import org.springdoc.api.annotations.responses.ApiResponses;


@CrossOrigin
@RestController
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private PatientRepo patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(AdminController.class);


    @Operation(summary = "Get Dashboard Counts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved counts"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/dashboard-counts")
    public ResponseEntity<Map<String, Long>> getDashboardCounts() {
        logger.info(RBundle.getKey(Constants.ADMIN_DASHBOARD_COUNTS_FETCH_START));
        Map<String, Long> counts = adminService.getDashboardCounts();
        logger.info(RBundle.getKey(Constants.ADMIN_DASHBOARD_COUNTS_FETCH_SUCCESS));
        return ResponseEntity.ok(counts);
    }

    @Operation(summary = "Get Appointment Statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved appointment statistics"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/appointment-statistics")
    public ResponseEntity<Map<String, Object>> getAppointmentStatistics() {
        logger.info("Fetching appointment statistics...");
        Map<String, Object> statistics = adminService.getAppointmentStatistics();
        logger.info("Appointment statistics fetched successfully.");
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Get Pending Doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pending doctors"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/pending-approvals")
    public ResponseEntity<List<Doctors>> getPendingDoctors() {
        logger.info(RBundle.getKey(Constants.DOCTOR_APPROVALS_PENDING_FETCH_START));
        List<Doctors> pendingDoctors = doctorService.getDoctorsByApprovalStatus(Doctors.ApprovalStatus.Pending);
        logger.info(RBundle.getKey(Constants.DOCTOR_APPROVALS_PENDING_FETCH_SUCCESS));
        return ResponseEntity.ok(pendingDoctors);
    }

    @Operation(summary = "Approve a Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor approved successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/approve-doctor/{doctorId}")
    public ResponseEntity<String> approveDoctor(@PathVariable Integer doctorId) {
        logger.info(RBundle.getKey(Constants.DOCTOR_APPROVE_START));
        doctorService.updateApprovalStatus(doctorId, Doctors.ApprovalStatus.Approved);
        logger.info(RBundle.getKey(Constants.DOCTOR_APPROVE_SUCCESS));
        return ResponseEntity.ok(RBundle.getKey(Constants.DOCTOR_APPROVE_RESPONSE));
    }

    @Operation(summary = "Reject a Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor rejected successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/reject-doctor/{doctorId}")
    public ResponseEntity<String> rejectDoctor(@PathVariable Integer doctorId) {
        logger.info(RBundle.getKey(Constants.DOCTOR_REJECT_START));
        doctorService.updateApprovalStatus(doctorId, Doctors.ApprovalStatus.Rejected);
        logger.info(RBundle.getKey(Constants.DOCTOR_REJECT_SUCCESS));
        return ResponseEntity.ok(RBundle.getKey(Constants.DOCTOR_REJECT_RESPONSE));
    }
}


