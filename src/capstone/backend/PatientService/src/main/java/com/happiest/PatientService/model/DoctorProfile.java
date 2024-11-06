package com.happiest.PatientService.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happiest.PatientService.dto.Doctors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.TreeSet;

@Entity
@Table(name = "doctorprofile")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Integer availabilityId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
    private Doctors doctor;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(name = "time_block_start", nullable = false)
    private LocalTime timeBlockStart;

    @Column(name = "time_block_end", nullable = false)
    private LocalTime timeBlockEnd;

    @Column(name = "slot_duration", nullable = false)
    private int slotDuration;

    @Column(name = "available_time_slots", columnDefinition = "JSON")
    private String availableTimeSlots; // Handle JSON parsing

    @Transient
    private List<LocalTime> availableTimeSlotsList;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostLoad
    @PostPersist
    @PostUpdate
//    private void convertJsonToList() {
//        try {
//            List<String> timeStrings = objectMapper.readValue(availableTimeSlots, new TypeReference<List<String>>() {});
//            availableTimeSlotsList = timeStrings.stream()
//                    .map(time -> LocalTime.parse(time.split(" - ")[0])) // Parse only the start time
//                    .sorted() // Sort the list by increasing order of time
//                    .collect(Collectors.toList());
//            log.info("Parsed and sorted available time slots: {}", availableTimeSlotsList);
//        } catch (Exception e) {
//            log.error("Error parsing available time slots JSON: {}", e.getMessage());
//            availableTimeSlotsList = Collections.emptyList();
//        }
//    }
    private void convertJsonToList() {
        try {
            List<String> timeStrings = objectMapper.readValue(availableTimeSlots, new TypeReference<List<String>>() {});

            // Use a TreeSet to automatically sort and remove duplicates
            Set<LocalTime> timeSet = timeStrings.stream()
                    .map(time -> LocalTime.parse(time.split(" - ")[0])) // Parse only the start time
                    .collect(Collectors.toCollection(TreeSet::new)); // TreeSet sorts and removes duplicates

            availableTimeSlotsList = new ArrayList<>(timeSet); // Convert back to a List if needed

            log.info("Parsed, sorted, and unique available time slots: {}", availableTimeSlotsList);
        } catch (Exception e) {
            log.error("Error parsing available time slots JSON: {}", e.getMessage());
            availableTimeSlotsList = Collections.emptyList();
        }
    }


}

