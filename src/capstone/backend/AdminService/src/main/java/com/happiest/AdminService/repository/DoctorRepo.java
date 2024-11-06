package com.happiest.AdminService.repository;

import com.happiest.AdminService.model.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctors,Integer> {
    List<Doctors> findByApprovalStatus(Doctors.ApprovalStatus status);
    long countByApprovalStatus(Doctors.ApprovalStatus approvalStatus);


}
