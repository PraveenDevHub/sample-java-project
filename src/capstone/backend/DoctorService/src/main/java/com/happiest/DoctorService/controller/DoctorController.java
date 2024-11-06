package com.happiest.DoctorService.controller;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.exception.DoctorNotFoundException;
import com.happiest.DoctorService.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/doctor")
@CrossOrigin
public class DoctorController {

    private static final Logger logger = LogManager.getLogger(DoctorController.class);

    @Autowired
    DoctorService doctorService;

    @GetMapping("/welcomedoctor")
    public String greet() {
        return "This is Doctor Service";
    }

    @GetMapping("/getDoctor/{doctorId}")
    @Operation(summary = "Fetch doctor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND)
    })
    public ResponseEntity<Doctors> getDoctorById(@PathVariable Integer doctorId) {
        logger.info(Constants.FETCHING_DOCTOR, doctorId);
        Doctors doctor = null;
        try {
            doctor = doctorService.findById(doctorId)
                    .orElseThrow(() -> new DoctorNotFoundException(Constants.DOCTOR_NOT_FOUND_WITH_ID + doctorId));
        } catch (DoctorNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.info(Constants.DOCTOR_FOUND, doctorId);
        return ResponseEntity.ok(doctor); // Return the doctor with HTTP 200 OK status
    }


    @PutMapping("/updateDoctor/{doctorId}")
    @Operation(summary = "Update doctor profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "404", description = Constants.DOCTOR_NOT_FOUND)
    })
    public ResponseEntity<Doctors> updateDoctorProfile(
            @PathVariable Integer doctorId,
            @RequestParam(required = false) MultipartFile profilePhoto,
            @RequestParam String name,
            @RequestParam String specialization,
            @RequestParam Integer yearsOfExperience,
            @RequestParam String doctorDescription,
            @RequestParam String hospitalName,
            @RequestParam String state,
            @RequestParam String city) {
        logger.info(Constants.UPDATING_DOCTOR_PROFILE, doctorId);
        Doctors updatedDoctor = doctorService.updateDoctorProfile(
                doctorId, profilePhoto, name, specialization, yearsOfExperience, doctorDescription, hospitalName, state, city);
        logger.info(Constants.DOCTOR_PROFILE_UPDATED, doctorId);
        return ResponseEntity.ok(updatedDoctor);
    }
}