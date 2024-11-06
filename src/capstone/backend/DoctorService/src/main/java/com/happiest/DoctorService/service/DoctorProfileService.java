package com.happiest.DoctorService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.exception.DuplicateScheduleException;
import com.happiest.DoctorService.exception.ResourceNotFoundException;
import com.happiest.DoctorService.model.DefaultSchedule;
import com.happiest.DoctorService.model.DoctorProfile;
import com.happiest.DoctorService.repository.DefaultScheduleRepository;
import com.happiest.DoctorService.repository.DoctorProfileRepository;
import com.happiest.DoctorService.repository.DoctorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DoctorProfileService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorProfileService.class);

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private DefaultScheduleRepository defaultScheduleRepository;

    @Autowired
    private DoctorRepo doctorRepo;

    public DoctorProfile createDoctorProfile(DoctorProfile doctorProfile) {
        if (doctorProfile.getTimeBlockStart().equals(doctorProfile.getTimeBlockEnd()) ||
                doctorProfile.getTimeBlockStart().isAfter(doctorProfile.getTimeBlockEnd())) {
            throw new IllegalArgumentException(Constants.INVALID_TIME_BLOCK);
        }

        // Calculate the duration of the time block in minutes
        long timeBlockDuration = Duration.between(doctorProfile.getTimeBlockStart(), doctorProfile.getTimeBlockEnd()).toMinutes();

        // Check if slot duration exceeds the time block duration
        if (doctorProfile.getSlotDuration() > timeBlockDuration) {
            throw new IllegalArgumentException(Constants.INVALID_SLOT_DURATION);
        }

        // Check if the schedule time is in the past for the current day
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        if (doctorProfile.getAvailableDate().isEqual(today) && doctorProfile.getTimeBlockStart().isBefore(now)) {
            throw new IllegalArgumentException(Constants.INVALID_PAST_TIME);
        }

        List<DoctorProfile> existingSchedules = doctorProfileRepository.findByDoctor_DoctorIdAndAvailableDateAndTimeBlockStartAndTimeBlockEnd(
                doctorProfile.getDoctor().getDoctorId(),
                doctorProfile.getAvailableDate(),
                doctorProfile.getTimeBlockStart(),
                doctorProfile.getTimeBlockEnd()
        );

        if (!existingSchedules.isEmpty()) {
            throw new DuplicateScheduleException(Constants.DUPLICATE_SCHEDULE);
        }

        return doctorProfileRepository.save(doctorProfile);
    }

    public List<DoctorProfile> getAllDoctorProfiles() {
        List<DoctorProfile> profiles = doctorProfileRepository.findAllDoctorProfiles();
        if (profiles.isEmpty()) {
            throw new ResourceNotFoundException(Constants.NO_DOCTOR_PROFILES_FOUND);
        }
        return profiles;
    }


    @Transactional
    public void saveDefaultSchedule(Map<String, Object> defaultSchedule) {
        try {
            Integer doctorId = (Integer) defaultSchedule.get("doctorId");
            logger.info(Constants.LOG_SAVING_DEFAULT_SCHEDULE, doctorId);
            Optional<Doctors> doctorOptional = doctorRepo.findById(doctorId);
            if (doctorOptional.isEmpty()) {
                throw new RuntimeException(Constants.DOCTOR_NOT_FOUND + doctorId);
            }
            Doctors doctor = doctorOptional.get();
            Map<String, List<Map<String, Object>>> scheduleMap = (Map<String, List<Map<String, Object>>>) defaultSchedule.get("defaultSchedule");
            if (scheduleMap == null) {
                throw new RuntimeException(Constants.INVALID_DEFAULT_SCHEDULE_FORMAT);
            }
            // Clear existing schedules for the doctor and day
            scheduleMap.forEach((day, timeBlocks) -> {
                defaultScheduleRepository.deleteByDoctor_DoctorIdAndDayOfWeek(doctorId, day);
                timeBlocks.forEach(scheduleDetails -> {
                    String start = (String) scheduleDetails.get("start");
                    String end = (String) scheduleDetails.get("end");
                    String duration = (String) scheduleDetails.get("duration");
                    List<String> availableTimeSlots = (List<String>) scheduleDetails.get("availableTimeSlots"); // Get slots from frontend
                    if (start != null && end != null && duration != null && availableTimeSlots != null) {
                        LocalTime startTime = LocalTime.parse(start);
                        LocalTime endTime = LocalTime.parse(end);
                        int slotDuration = Integer.parseInt(duration);

                        // Validate start time is not after end time
                        if (startTime.isAfter(endTime)) {
                            throw new IllegalArgumentException(Constants.INVALID_TIME_BLOCK);
                        }

                        // Validate slot duration does not exceed the time block duration
                        long timeBlockDuration = Duration.between(startTime, endTime).toMinutes();
                        if (slotDuration > timeBlockDuration) {
                            throw new IllegalArgumentException(Constants.INVALID_SLOT_DURATION);
                        }

                        DefaultSchedule defaultScheduleEntity = new DefaultSchedule();
                        defaultScheduleEntity.setDoctor(doctor);
                        defaultScheduleEntity.setDayOfWeek(day);
                        defaultScheduleEntity.setTimeBlockStart(startTime);
                        defaultScheduleEntity.setTimeBlockEnd(endTime);
                        defaultScheduleEntity.setSlotDuration(slotDuration);
                        try {
                            // Set available time slots received from frontend
                            defaultScheduleEntity.setAvailableTimeSlots(new ObjectMapper().writeValueAsString(availableTimeSlots));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(Constants.ERROR_CONVERTING_TIME_SLOTS + day, e);
                        }
                        defaultScheduleRepository.save(defaultScheduleEntity);
                    } else {
                        logger.error(Constants.INVALID_SCHEDULE_DETAILS, day);
                        throw new RuntimeException(Constants.INVALID_SCHEDULE_DETAILS + day);
                    }
                });
            });
        } catch (Exception e) {
            logger.error(Constants.FAILED_TO_SAVE_DEFAULT_SCHEDULE, e);
            throw new RuntimeException(Constants.FAILED_TO_SAVE_DEFAULT_SCHEDULE, e);
        }
    }

    public List<DefaultSchedule> getDefaultScheduleForDay(Integer doctorId, String dayOfWeek) {
        List<DefaultSchedule> schedules = defaultScheduleRepository.findByDoctor_DoctorIdAndDayOfWeek(doctorId, dayOfWeek);
        if (schedules.isEmpty()) {
            throw new ResourceNotFoundException(String.format(Constants.NO_SCHEDULE_FOUND_FOR_DAY, dayOfWeek));
        }
        return schedules;
    }

}
