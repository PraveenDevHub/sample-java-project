package com.happiest.PatientService.model;

import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "doctor_id" , referencedColumnName = "doctor_id", nullable = false)
    private Doctors doctor; // Many appointments can be associated with one doctor

    @ManyToOne
    @JoinColumn(name = "patient_id" , referencedColumnName = "patient_id", nullable = false)
    private Patients patient; // Many appointments can be associated with one patient

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time_slot", nullable = false)
    private LocalTime appointmentTimeSlot;

    @Column(name = "reason_for_visit", length = 255)
    private String reasonForVisit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('Scheduled','Cancelled','Completed')", nullable = false)
    private AppointmentStatus status = AppointmentStatus.Scheduled; // Default to 'Scheduled'


    public enum AppointmentStatus {
        Scheduled, Cancelled, Completed
    }
}
