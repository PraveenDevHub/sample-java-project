package com.happiest.APIGatewayJWT2.apigateway;

import com.happiest.APIGatewayJWT2.model.Appointments;
import com.happiest.APIGatewayJWT2.model.Doctors;
import com.happiest.APIGatewayJWT2.model.Patients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@FeignClient(name = "http://PatientService")
public interface PatientServiceInterface {
    @GetMapping("/greet")
    public String greet();

    @GetMapping("/appoi/appointment/history/{patientId}")
    ResponseEntity<List<Appointments>> getAppointmentHistory(@PathVariable("patientId") int patientId);

    @PutMapping("/appoi/appointment/cancel/{appointmentId}/{patientId}")
    ResponseEntity<Void> cancelAppointment(@PathVariable("appointmentId") int appointmentId,
                                           @PathVariable("patientId") int patientId);

    @GetMapping("/appoi/appointment/upcoming/{patientId}")
    ResponseEntity<List<Appointments>> getPatientUpcomingAppointments(@PathVariable("patientId") int patientId);

    @GetMapping("/appoi/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@RequestParam Integer doctorId);

    @GetMapping("/appoi/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(@RequestParam Integer doctorId, @RequestParam String date);

    @PostMapping("/appoi/book")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointments appointment);

    @GetMapping("/patients/doctors")
    List<Doctors> getDoctors(@RequestParam(required = false) Integer limit);

    @GetMapping("/patients/doctors/filter")
    public ResponseEntity<List<Doctors>> filterDoctors(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String hospital,
            @RequestParam(required = false) String searchTerm);

    @GetMapping("/patients/{patientId}")
    ResponseEntity<Patients> getPatientById(@PathVariable("patientId") Integer patientId);

    @PostMapping("/patients/appointments")
    ResponseEntity<Appointments> createAppointment(@RequestBody Appointments appointment);

    @GetMapping("/patients/appointments/{patientId}")
    ResponseEntity<List<Appointments>> getAppointmentsByPatientId(@PathVariable("patientId") Integer patientId);

    @GetMapping("/patients/getPatientProfile/{patientId}")
    ResponseEntity<Patients> getPatientProfile(@PathVariable Integer patientId);

    @PutMapping("/patients/updatePatientProfile/{patientId}")
    public ResponseEntity<Patients> updatePatientProfile(@PathVariable Integer patientId, @RequestBody Patients updatedPatient);

}