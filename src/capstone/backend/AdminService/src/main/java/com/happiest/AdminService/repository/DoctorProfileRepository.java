package com.happiest.AdminService.repository;

import com.happiest.AdminService.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Integer> {
    // Additional query methods if needed
}

