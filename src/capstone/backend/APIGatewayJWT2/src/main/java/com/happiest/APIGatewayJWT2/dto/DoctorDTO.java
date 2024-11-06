package com.happiest.APIGatewayJWT2.dto;


import com.happiest.APIGatewayJWT2.model.Doctors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    private Integer doctorId;
    private String name;
    private String specialization;
    private String medicalLicenseNumber;
    private Doctors.ApprovalStatus approvalStatus;
    private String hospitalName; // New field
    private int yearsOfExperience; // New field
    private String email; // New field
}
