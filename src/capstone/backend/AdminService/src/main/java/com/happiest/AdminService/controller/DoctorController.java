package com.happiest.AdminService.controller;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.service.DoctorService;
import com.happiest.AdminService.utility.RBundle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/doctorsdisplay")
//@Api(value = "Doctor Controller", tags = "Doctor Management")

public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(DoctorController.class);

    @Operation(summary = "Get All Doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of doctors"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(@RequestParam(required = false) Doctors.ApprovalStatus approvalStatus) {
        logger.info(RBundle.getKey(Constants.DOCTOR_FETCH_START));
        List<DoctorDTO> doctors = doctorService.getAllDoctors(approvalStatus);
        logger.info(RBundle.getKey(Constants.DOCTOR_FETCH_SUCCESS));
        return ResponseEntity.ok(doctors);
    }
}

