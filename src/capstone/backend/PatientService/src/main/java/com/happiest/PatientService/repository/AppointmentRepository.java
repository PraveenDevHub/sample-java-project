package com.happiest.PatientService.repository;

import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Integer> {
    // Additional query methods if needed
    @Query("SELECT a FROM Appointments a WHERE a.patient.patientId = :patientId AND a.status IN :statuses ORDER BY a.appointmentDate DESC, a.appointmentTimeSlot ASC")
    List<Appointments> findByPatientIdAndStatus(@Param("patientId") int patientId, @Param("statuses") List<Appointments.AppointmentStatus> statuses);

    //
    @Query("SELECT a FROM Appointments a WHERE a.appointmentDate = :appointmentDate AND a.status = :status")
    List<Appointments> findByAppointmentDateAndStatus(@Param("appointmentDate") LocalDate appointmentDate,
                                                      @Param("status") Appointments.AppointmentStatus status);

    @Query("SELECT a FROM Appointments a WHERE a.appointmentDate = :appointmentDate AND a.appointmentTimeSlot = :appointmentTimeSlot AND a.status = :status")
    List<Appointments> findByAppointmentDateAndTimeSlotAndStatus(@Param("appointmentDate") LocalDate appointmentDate,
                                                                 @Param("appointmentTimeSlot") LocalTime appointmentTimeSlot,
                                                                 @Param("status") Appointments.AppointmentStatus status);

    @Query("SELECT a FROM Appointments a WHERE a.patient.patientId = ?1 AND a.appointmentDate >= ?2 AND a.status = 'Scheduled' ORDER BY a.appointmentDate ASC, a.appointmentTimeSlot ASC")
    List<Appointments> findUpcomingAppointments(int patientId, LocalDate currentDate);

    List<Appointments> findByDoctorAndAppointmentDate(Doctors doctor, LocalDate date);


}
