package com.happiest.APIGatewayJWT2.repository;

import com.happiest.APIGatewayJWT2.model.Patients;
import com.happiest.APIGatewayJWT2.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepo extends JpaRepository<Patients, Integer> {
    Patients findByUser(Users user);
}