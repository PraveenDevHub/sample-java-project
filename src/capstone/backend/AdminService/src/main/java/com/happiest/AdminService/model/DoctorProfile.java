package com.happiest.AdminService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctorprofile")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}

