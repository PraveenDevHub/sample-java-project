package com.happiest.DoctorService.repository;

import com.happiest.DoctorService.model.DefaultSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefaultScheduleRepository extends JpaRepository<DefaultSchedule, Integer> {
    List<DefaultSchedule> findByDoctor_DoctorIdAndDayOfWeek(Integer doctorId, String dayOfWeek);
    void deleteByDoctor_DoctorIdAndDayOfWeek(Integer doctorId, String dayOfWeek);
}

