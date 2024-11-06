package com.happiest.APIGatewayJWT2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Users user;

    @Column(name = "medical_license_number", nullable = false, unique = true)
    private String medicalLicenseNumber;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.Pending; // Default to 'Pending'

    public enum ApprovalStatus {
        Pending, Approved, Rejected
    }

    @Column(name = "doctor_description")
    private String doctorDescription;

    @Column(name = "profile_photo")
    private String profilePhoto;

}
