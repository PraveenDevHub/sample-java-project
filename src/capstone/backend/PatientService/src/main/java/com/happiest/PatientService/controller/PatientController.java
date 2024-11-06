package com.happiest.PatientService.controller;

import com.happiest.PatientService.constants.Constants;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.exceptions.DoctorNotFoundException;
import com.happiest.PatientService.exceptions.PatientNotFoundException;
import com.happiest.PatientService.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    private static final Logger logger = LogManager.getLogger(PatientController.class.getName());


    @Operation(summary = "Get list of doctors", description = "Fetches all doctors or a limited number if a limit is provided.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.DOCTORS_RETRIEVED),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctors>> getDoctors(@RequestParam(required = false) Integer limit) {
        logger.info(Constants.LOG_FETCH_DOCTORS, limit);
        List<Doctors> doctors = patientService.getDoctors(limit);
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Filter doctors", description = "Filters doctors based on state, city, specialization, hospital, or search term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.DOCTORS_FILTERED_RETRIEVED),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/doctors/filter")
    public ResponseEntity<List<Doctors>> filterDoctors(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String hospital,
            @RequestParam(required = false) String searchTerm) {

        logger.info(Constants.LOG_FILTER_DOCTORS, state, city, specialization, hospital, searchTerm);
        List<Doctors> filteredDoctors = patientService.filterDoctors(state, city, specialization, hospital, searchTerm);
        return ResponseEntity.ok(filteredDoctors);
    }

    @Operation(summary = "Get patient profile", description = "Fetches the profile details of a specific patient by patient ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.PATIENT_PROFILE_RETRIEVED),
            @ApiResponse(responseCode = "404", description = Constants.PATIENT_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/getPatientProfile/{patientId}")
    public ResponseEntity<Patients> getPatientProfile(@PathVariable Integer patientId) {
        logger.info(Constants.LOG_GET_PATIENT_PROFILE, patientId);
        Optional<Patients> patientOptional = patientService.findById(patientId);

        if (patientOptional.isPresent()) {
            return ResponseEntity.ok(patientOptional.get());
        } else {
            logger.warn("Patient not found with id: {}", patientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // or you can return a custom error message object
        }
    }
    @Operation(summary = "Update patient profile", description = "Updates the profile details of a specific patient by patient ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.PATIENT_PROFILE_UPDATED),
            @ApiResponse(responseCode = "404", description = Constants.PATIENT_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = Constants.INTERNAL_SERVER_ERROR)
    })
    @PutMapping("/updatePatientProfile/{patientId}")
    public ResponseEntity<Patients> updatePatientProfile(@PathVariable Integer patientId, @RequestBody Patients updatedPatient) {
        logger.info(Constants.LOG_UPDATE_PATIENT_PROFILE, patientId);
        Patients patient = patientService.updatePatientProfile(patientId, updatedPatient);
        return ResponseEntity.ok(patient);
    }

}
