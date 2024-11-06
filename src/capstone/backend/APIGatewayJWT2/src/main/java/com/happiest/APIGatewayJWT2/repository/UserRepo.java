package com.happiest.APIGatewayJWT2.repository;

import com.happiest.APIGatewayJWT2.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    Users findByName(String name);

    Optional<Users> findByEmail(String email);
}