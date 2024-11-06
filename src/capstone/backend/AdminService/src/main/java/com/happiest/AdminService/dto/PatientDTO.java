package com.happiest.AdminService.dto;

import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.model.Patients;
import com.happiest.AdminService.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private Integer patientId;
    private String name;
    private int age;
    private Patients.Gender gender;
    private String email;
}
