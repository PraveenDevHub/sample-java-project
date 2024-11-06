package com.happiest.PatientService.repository;

import com.happiest.PatientService.dto.Patients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository <Patients,Integer>{
}
