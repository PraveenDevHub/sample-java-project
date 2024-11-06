package com.happiest.DoctorService.repository;

import com.happiest.DoctorService.dto.Patients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository <Patients,Integer>{
}
