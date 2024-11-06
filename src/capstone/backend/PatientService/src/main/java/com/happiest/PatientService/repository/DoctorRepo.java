package com.happiest.PatientService.repository;

import com.happiest.PatientService.dto.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctors,Integer> {
}
