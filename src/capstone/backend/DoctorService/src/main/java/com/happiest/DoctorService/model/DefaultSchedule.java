package com.happiest.DoctorService.model;

import com.happiest.DoctorService.dto.Doctors;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "default_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
    private Doctors doctor;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "time_block_start", nullable = false)
    private LocalTime timeBlockStart;

    @Column(name = "time_block_end", nullable = false)
    private LocalTime timeBlockEnd;

    @Column(name = "slot_duration", nullable = false)
    private int slotDuration;

    @Column(name = "available_time_slots", columnDefinition = "JSON")
    private String availableTimeSlots; // Handle JSON parsing of time slots


}
