package com.happiest.DoctorService.controller;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.model.*;
import com.happiest.DoctorService.service.DoctorProfileService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctorprofiles")
public class DoctorProfileController {

    private static final Logger logger = LogManager.getLogger(DoctorProfileController.class.getName());
    @Autowired
    private DoctorProfileService doctorProfileService;

    @PostMapping("/schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_CREATION),
            @ApiResponse(responseCode = "409", description = Constants.DUPLICATE_SCHEDULE),
            @ApiResponse(responseCode = "500", description = Constants.FAILED_CREATION)
    })
    public ResponseEntity<?> createDoctorProfile(@RequestBody DoctorProfile doctorProfile) {
        logger.info(Constants.LOG_CREATE_DOCTOR_PROFILE, doctorProfile.getDoctor().getDoctorId());
        DoctorProfile createdProfile = doctorProfileService.createDoctorProfile(doctorProfile);
        logger.info(Constants.LOG_DOCTOR_PROFILE_CREATED, doctorProfile.getDoctor().getDoctorId());
        return ResponseEntity.ok(createdProfile);
    }


    @PostMapping("/defaultschedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_SAVE),
            @ApiResponse(responseCode = "500", description = Constants.DEFAULT_SCHEDULE_SAVE_ERROR)
    })
    public ResponseEntity<String> saveDefaultSchedule(@RequestBody Map<String, Object> defaultSchedule) {
        Integer doctorId = (Integer) defaultSchedule.get("doctorId");
        logger.info(Constants.LOG_SAVING_DEFAULT_SCHEDULE, doctorId);
        doctorProfileService.saveDefaultSchedule(defaultSchedule);
        logger.info(Constants.LOG_DEFAULT_SCHEDULE_SAVED, doctorId);
        return ResponseEntity.ok(Constants.SUCCESSFUL_SAVE);
    }


    @GetMapping("/defaultschedule/{doctorId}/{dayOfWeek}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "500", description = Constants.DEFAULT_SCHEDULE_FETCH_ERROR)
    })
    public ResponseEntity<List<DefaultSchedule>> getDefaultScheduleForDay(@PathVariable Integer doctorId, @PathVariable String dayOfWeek) {
        logger.info(Constants.LOG_FETCHING_DEFAULT_SCHEDULE, doctorId, dayOfWeek);
        List<DefaultSchedule> schedules = doctorProfileService.getDefaultScheduleForDay(doctorId, dayOfWeek);
        logger.info(Constants.LOG_DEFAULT_SCHEDULE_FETCHED, doctorId, dayOfWeek);
        return ResponseEntity.ok(schedules);
    }


    @GetMapping("/getallprofiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = Constants.SUCCESSFUL_RETRIEVAL),
            @ApiResponse(responseCode = "500", description = Constants.DOCTOR_PROFILES_FETCH_ERROR)
    })
    public ResponseEntity<List<DoctorProfile>> getAllDoctorProfiles() {
        logger.info(Constants.LOG_FETCHING_ALL_PROFILES);
        List<DoctorProfile> profiles = doctorProfileService.getAllDoctorProfiles();
        logger.info(Constants.LOG_ALL_PROFILES_FETCHED);
        return ResponseEntity.ok(profiles);
    }

}
