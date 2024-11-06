package com.happiest.AdminService.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.service.AdminService;
import com.happiest.AdminService.service.DoctorService;
import com.happiest.AdminService.model.Doctors;

import com.happiest.AdminService.utility.RBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Mock
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDashboardCounts() {
        // Arrange
        HashMap<String, Long> counts = new HashMap<>();
        counts.put("doctors", 10L);
        when(adminService.getDashboardCounts()).thenReturn(counts);

        // Act
        ResponseEntity<Map<String, Long>> response = adminController.getDashboardCounts();

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(counts, response.getBody());
    }

    @Test
    void testGetPendingDoctors() {
        // Arrange
        List<Doctors> pendingDoctors = List.of(new Doctors());
        when(doctorService.getDoctorsByApprovalStatus(Doctors.ApprovalStatus.Pending)).thenReturn(pendingDoctors);

        // Act
        ResponseEntity<List<Doctors>> response = adminController.getPendingDoctors();

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(pendingDoctors, response.getBody());
    }

    @Test
    void testApproveDoctor() {
        // Arrange
        Integer doctorId = 1;
        String expectedResponse = RBundle.getKey(Constants.DOCTOR_APPROVE_RESPONSE);

        // Act
        ResponseEntity<String> response = adminController.approveDoctor(doctorId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(doctorService, times(1)).updateApprovalStatus(doctorId, Doctors.ApprovalStatus.Approved);
    }

    @Test
    void testRejectDoctor() {
        // Arrange
        Integer doctorId = 1;
        String expectedResponse = RBundle.getKey(Constants.DOCTOR_REJECT_RESPONSE);

        // Act
        ResponseEntity<String> response = adminController.rejectDoctor(doctorId);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(doctorService, times(1)).updateApprovalStatus(doctorId, Doctors.ApprovalStatus.Rejected);
    }

//    @Test
//    public void testGetDashboardCounts_throwsException() {
//        // Arrange
//        Mockito.when(adminService.getDashboardCounts()).thenThrow(new RuntimeException("Service error"));
//
//        // Act
//        ResponseEntity<Map<String, Long>> response = adminController.getDashboardCounts();
//
//        // Assert
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }

//    @Test
//    public void testGetPendingDoctors_throwsException() {
//        // Arrange
//        Mockito.when(doctorService.getDoctorsByApprovalStatus(Doctors.ApprovalStatus.Pending))
//                .thenThrow(new RuntimeException("Service error"));
//
//        // Act
//        ResponseEntity<List<Doctors>> response = adminController.getPendingDoctors();
//
//        // Assert
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }


}

