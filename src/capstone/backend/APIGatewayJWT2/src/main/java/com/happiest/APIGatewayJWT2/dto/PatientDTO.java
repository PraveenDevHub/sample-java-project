package com.happiest.APIGatewayJWT2.dto;

import com.happiest.APIGatewayJWT2.model.Patients;
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
