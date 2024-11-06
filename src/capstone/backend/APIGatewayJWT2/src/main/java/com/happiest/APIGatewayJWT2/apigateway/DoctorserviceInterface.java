package com.happiest.APIGatewayJWT2.apigateway;

import com.happiest.APIGatewayJWT2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "http://DoctorService")

public interface DoctorserviceInterface {

    @GetMapping("doctor/welcomedoctor")
    public String greet();

    @PostMapping("doctorprofiles/schedule")
    ResponseEntity<?> createDoctorProfile(@RequestBody DoctorProfile doctorProfile);

    @PostMapping("doctorprofiles/defaultschedule")
    public void saveDefaultSchedule(@RequestBody Map<String, Object> defaultSchedule);

    @GetMapping("doctorprofiles/defaultschedule/{doctorId}/{dayOfWeek}")
    public List<DefaultSchedule> getDefaultScheduleForDay(@PathVariable("doctorId") Integer doctorId, @PathVariable("dayOfWeek") String dayOfWeek);

    @GetMapping("doctorprofiles/getallprofiles")
    public List<DoctorProfile> getAllDoctorProfiles();

    @PostMapping("appoi/create")
    ResponseEntity<Appointments> createAppointment(@RequestBody Appointments appointment);

    // Get upcoming appointments for a specific doctor
    @GetMapping("/appoi/{doctorId}/appointments")
    ResponseEntity<List<Appointments>> getUpcomingAppointments(@PathVariable("doctorId") int doctorId);

    // Cancel a specific appointment
    @PutMapping("/appoi/appointment/{appointmentId}/cancel")
    ResponseEntity<Void> cancelAppointment(@PathVariable("appointmentId") int appointmentId);

    // Mark an appointment as completed
    @PutMapping("/appoi/{appointmentId}/complete")
    ResponseEntity<Void> completeAppointment(@PathVariable("appointmentId") int appointmentId);

    @GetMapping("/doctor/getDoctor/{doctorId}")
    ResponseEntity<Doctors> getDoctorById(@PathVariable Integer doctorId);

    @GetMapping("appoi/appointment/history/{doctorId}")
    public ResponseEntity<List<Appointments>> getDoctorAppointmentHistory(@PathVariable("doctorId") int doctorId);

    @PutMapping("appoi/{doctorId}/cancelAll")
    public ResponseEntity<String> cancelAllAppointments(@PathVariable("doctorId") Integer doctorId, @RequestBody Map<String, String> request);

    @GetMapping("/user/getUser/{userId}")
    ResponseEntity<Users> getUserById(@PathVariable Integer userId);


//    // Mark past appointments as completed (optional endpoint for manual triggering)
//    @PutMapping("/appoi/complete-past")
//    ResponseEntity<Void> markPastAppointmentsAsCompleted();

//    @GetMapping("/appoi")
//    public ResponseEntity<List<Appointments>> getAppointments(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);



    }
