package com.happiest.PatientService.repository;

import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.model.DoctorProfile;
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

    List<DoctorProfile> findByDoctor(Doctors doctor);

    List<DoctorProfile> findByDoctorAndAvailableDate(Doctors doctor, LocalDate date);

    //After booking appointment removing that slot from available_slots
    @Modifying
    @Transactional
    @Query(value = "UPDATE doctorprofile " +
            "SET available_time_slots = JSON_REMOVE(available_time_slots, JSON_UNQUOTE(JSON_SEARCH(available_time_slots, 'one', :timeSlot))) " +
            "WHERE doctor_id = :doctorId AND available_date = :availableDate", nativeQuery = true)
    int removeTimeSlot(@Param("doctorId") Integer doctorId,
                       @Param("availableDate") String availableDate,
                       @Param("timeSlot") String timeSlot);

    //after patient cancels appointment adding that slot to the available_slot
    @Modifying
    @Transactional
    @Query(value = "UPDATE doctorprofile " +
            "SET available_time_slots = JSON_ARRAY_APPEND(available_time_slots, '$', :timeSlot) " +
            "WHERE doctor_id = :doctorId AND available_date = :availableDate", nativeQuery = true)
    int addTimeSlot(@Param("doctorId") int doctorId,
                    @Param("availableDate") String availableDate,
                    @Param("timeSlot") String timeSlot);


}

