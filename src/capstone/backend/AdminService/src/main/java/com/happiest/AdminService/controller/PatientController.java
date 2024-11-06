package com.happiest.AdminService.controller;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.PatientDTO;
import com.happiest.AdminService.service.PatientService;
import com.happiest.AdminService.utility.RBundle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/patientsdisplay")
//@Api(value = "Patient Controller", tags = "Patient Management")

public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(PatientController.class);

    @Operation(summary = "Get All Patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of patients"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        logger.info(RBundle.getKey(Constants.PATIENT_FETCH_START));
        List<PatientDTO> patients = patientService.getAllPatients();
        logger.info(RBundle.getKey(Constants.PATIENT_FETCH_SUCCESS));
        return ResponseEntity.ok(patients);
    }

}

