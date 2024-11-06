package com.happiest.PatientService.repository;

import com.happiest.PatientService.dto.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {
}
