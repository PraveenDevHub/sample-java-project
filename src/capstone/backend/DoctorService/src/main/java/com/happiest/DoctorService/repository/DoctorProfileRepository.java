package com.happiest.DoctorService.repository;

import com.happiest.DoctorService.model.DefaultSchedule;
import com.happiest.DoctorService.model.DoctorProfile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Integer> {
    List<DoctorProfile> findByDoctor_DoctorId(Integer doctorId);

    List<DoctorProfile> findByDoctor_DoctorIdAndAvailableDateAndTimeBlockStartAndTimeBlockEnd(
            Integer doctorId, LocalDate availableDate, LocalTime timeBlockStart, LocalTime timeBlockEnd);

    @Modifying
    @Transactional
    @Query("DELETE FROM DoctorProfile dp WHERE dp.doctor.doctorId = :doctorId AND dp.availableDate = :availableDate")
    void deleteByDoctorIdAndAvailableDate(@Param("doctorId") Integer doctorId, @Param("availableDate") LocalDate availableDate);

    @Query(value = "SELECT dp.availability_id, dp.doctor_id, dp.available_date, dp.time_block_start, dp.time_block_end, dp.slot_duration, JSON_EXTRACT(dp.available_time_slots, '$') AS available_time_slots FROM doctorprofile dp", nativeQuery = true)
    List<DoctorProfile> findAllDoctorProfiles();
}
