package com.happiest.AdminService;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.model.Users;
import com.happiest.AdminService.repository.AppointmentRepository;
import com.happiest.AdminService.repository.DoctorRepo;
import com.happiest.AdminService.repository.PatientRepo;
import com.happiest.AdminService.repository.UserRepo;
import com.happiest.AdminService.service.AdminService;
import com.happiest.AdminService.service.DoctorService;
import com.happiest.AdminService.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private PatientRepo patientRepository;

    @MockBean
    private DoctorRepo doctorRepository;

    @MockBean
    private UserRepo userRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @Test
    public void testGetDashboardCounts_success() {
        // Arrange
        Mockito.when(userRepository.count()).thenReturn(10L);
        Mockito.when(patientRepository.count()).thenReturn(20L);
        Mockito.when(doctorRepository.count()).thenReturn(30L);
        Mockito.when(appointmentRepository.count()).thenReturn(40L);
        Mockito.when(doctorRepository.countByApprovalStatus(Constants.APPROVAL_STATUS_PENDING)).thenReturn(5L);

        // Act
        Map<String, Long> counts = adminService.getDashboardCounts();

        // Assert
        assertEquals(10L, counts.get(Constants.ENTITY_USERS));
        assertEquals(20L, counts.get(Constants.ENTITY_PATIENTS));
        assertEquals(30L, counts.get(Constants.ENTITY_DOCTORS));
        assertEquals(40L, counts.get(Constants.ENTITY_APPOINTMENTS));
        assertEquals(5L, counts.get(Constants.ENTITY_APPROVALS));
    }

    @Test
    public void testGetDashboardCounts_failure() {
        // Arrange
        Mockito.when(userRepository.count()).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.getDashboardCounts());
    }

    @Test
    public void testGetDashboardCounts_throwsException() {
        // Arrange
        Mockito.when(userRepository.count()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> adminService.getDashboardCounts());
    }

}

