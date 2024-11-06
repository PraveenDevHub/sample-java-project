package com.happiest.AdminService.repository;

import com.happiest.AdminService.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
}