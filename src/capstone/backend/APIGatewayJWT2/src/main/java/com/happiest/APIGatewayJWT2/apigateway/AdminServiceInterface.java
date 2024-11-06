package com.happiest.APIGatewayJWT2.apigateway;

import com.happiest.APIGatewayJWT2.dto.DoctorDTO;
import com.happiest.APIGatewayJWT2.dto.PatientDTO;
import com.happiest.APIGatewayJWT2.model.Doctors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "http://AdminService")
public interface AdminServiceInterface {

    @GetMapping("admin/dashboard-counts")
    public ResponseEntity<Map<String, Long>> getDashboardCounts();

    @GetMapping("admin/appointment-statistics")
    public ResponseEntity<Map<String, Object>> getAppointmentStatistics() ;

    @GetMapping("admin/pending-approvals")
    public ResponseEntity<List<Doctors>> getPendingDoctors() ;

    @PutMapping("admin/approve-doctor/{doctorId}")
    public ResponseEntity<String> approveDoctor(@PathVariable Integer doctorId);

    @PutMapping("admin/reject-doctor/{doctorId}")
    public ResponseEntity<String> rejectDoctor(@PathVariable Integer doctorId);

    @GetMapping("/doctorsdisplay/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(@RequestParam(required = false) Doctors.ApprovalStatus approvalStatus) ;

    @GetMapping("/patientsdisplay/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() ;

}
