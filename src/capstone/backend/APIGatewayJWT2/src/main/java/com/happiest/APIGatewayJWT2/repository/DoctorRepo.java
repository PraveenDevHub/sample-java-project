package com.happiest.APIGatewayJWT2.repository;

import com.happiest.APIGatewayJWT2.model.Doctors;
import com.happiest.APIGatewayJWT2.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepo extends JpaRepository<Doctors, Integer> {
    Doctors findByUser(Users user);
}