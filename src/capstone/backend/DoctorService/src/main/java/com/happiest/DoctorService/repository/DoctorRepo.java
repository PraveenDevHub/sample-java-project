package com.happiest.DoctorService.repository;

import com.happiest.DoctorService.dto.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctors,Integer> {
}
