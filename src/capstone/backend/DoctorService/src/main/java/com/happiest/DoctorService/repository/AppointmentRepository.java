package com.happiest.DoctorService.repository;

import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.model.Appointments;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Integer> {
    // Additional query methods if needed
    List<Appointments> findByDoctorAndStatus(Doctors doctor, Appointments.AppointmentStatus status, Sort sort);

    @Query("SELECT a FROM Appointments a WHERE a.doctor.doctorId = :doctorId AND a.status IN ('Cancelled', 'Completed')")
    List<Appointments> findAppointmentHistoryByDoctorId(@Param("doctorId") int doctorId);

//    List<Appointments> findByAppointmentDate(LocalDate date);
//List<Appointments> findByDoctorIdAndAppointmentDate(Integer doctorId, LocalDate appointmentDate);
@Query("SELECT a FROM Appointments a WHERE a.doctor.doctorId = :doctorId AND a.appointmentDate = :appointmentDate")
List<Appointments> findByDoctorIdAndAppointmentDate(@Param("doctorId") Integer doctorId, @Param("appointmentDate") LocalDate appointmentDate);

    @Modifying // Indicates that this query modifies data
    @Transactional // Required for modifying operations
    @Query(value = "UPDATE appointments SET status = 'Completed' " +
            "WHERE (appointment_date < CURRENT_DATE " +
            "OR (appointment_date = CURRENT_DATE AND appointment_time_slot < CURRENT_TIME)) " +
            "AND status != 'Cancelled'", nativeQuery = true)
    void markPastAppointmentsAsCompleted();
}
