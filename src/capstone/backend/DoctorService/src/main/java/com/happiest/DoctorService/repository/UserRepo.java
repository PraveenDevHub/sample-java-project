package com.happiest.DoctorService.repository;

import com.happiest.DoctorService.dto.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Users findByName(String name);

    Optional<Users> findByEmail(String email);
}
