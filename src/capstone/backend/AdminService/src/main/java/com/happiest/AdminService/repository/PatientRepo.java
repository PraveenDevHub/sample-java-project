package com.happiest.AdminService.repository;

import com.happiest.AdminService.model.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepo extends JpaRepository <Patients,Integer>{
}
