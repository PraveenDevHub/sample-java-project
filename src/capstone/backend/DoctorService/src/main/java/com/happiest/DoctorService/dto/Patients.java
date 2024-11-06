package com.happiest.DoctorService.dto;

import com.happiest.DoctorService.dto.Users;
import com.happiest.DoctorService.model.Appointments;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "patients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column
    private String contact_number;

    public enum Gender {
        Male, Female, Other
    }

//    @OneToMany(mappedBy = "patient")
//    private List<Appointments> appointments;
}
