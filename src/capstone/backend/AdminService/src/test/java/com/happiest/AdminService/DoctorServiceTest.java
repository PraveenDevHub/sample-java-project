package com.happiest.AdminService;

import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.exception.InvalidOperationException;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.model.Users;
import com.happiest.AdminService.repository.DoctorRepo;
import com.happiest.AdminService.service.DoctorService;
import com.happiest.AdminService.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DoctorServiceTest {

    @Autowired
    private DoctorService doctorService;

    @MockBean
    private DoctorRepo doctorRepository;

    @MockBean
    private EmailService emailService;

    @Test
    public void testGetAllDoctors_withStatus() {
        // Arrange
        Users user = new Users();
        user.setName("Doctor Name");
        user.setEmail("doctor@example.com");

        Doctors doctor = new Doctors();
        doctor.setApprovalStatus(Doctors.ApprovalStatus.Approved);
        doctor.setUser(user); // Set the user for the doctor

        List<Doctors> doctorsList = Arrays.asList(doctor);

        Mockito.when(doctorRepository.findByApprovalStatus(Doctors.ApprovalStatus.Approved))
                .thenReturn(doctorsList);

        // Act
        List<DoctorDTO> result = doctorService.getAllDoctors(Doctors.ApprovalStatus.Approved);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals("Doctor Name", result.get(0).getName()); // Additional check
    }


    @Test
    public void testUpdateApprovalStatus_success() {
        // Arrange
        Doctors doctor = new Doctors();
        doctor.setApprovalStatus(Doctors.ApprovalStatus.Pending);
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setName("Doctor Name");
        doctor.setUser(user);

        Mockito.when(doctorRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(doctor));
        Mockito.when(doctorRepository.save(Mockito.any(Doctors.class))).thenReturn(doctor);

        // Act
        doctorService.updateApprovalStatus(1, Doctors.ApprovalStatus.Approved);

        // Assert
        Mockito.verify(doctorRepository, Mockito.times(1)).save(Mockito.any(Doctors.class));
        Mockito.verify(emailService, Mockito.times(1)).sendEmail(
                Mockito.eq("test@example.com"),
                Mockito.anyString(),
                Mockito.anyString());
    }

    @Test
    public void testUpdateApprovalStatus_failure() {
        // Arrange
        Mockito.when(doctorRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateApprovalStatus(1, Doctors.ApprovalStatus.Approved));
    }

    @Test
    public void testGetPendingApprovals_success() {
        // Arrange
        Doctors doctor = new Doctors();
        doctor.setApprovalStatus(Doctors.ApprovalStatus.Pending);

        // Create and set a User for the Doctor
        Users user = new Users();
        user.setName("Test Doctor");  // Set the name or other necessary fields
        doctor.setUser(user); // Set the User in the Doctor

        List<Doctors> doctorsList = Arrays.asList(doctor);

        Mockito.when(doctorRepository.findByApprovalStatus(Doctors.ApprovalStatus.Pending))
                .thenReturn(doctorsList);

        // Act
        List<DoctorDTO> result = doctorService.getPendingApprovals();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(Doctors.ApprovalStatus.Pending, result.get(0).getApprovalStatus());
        assertEquals("Test Doctor", result.get(0).getName()); // Ensure name matches
    }

    @Test
    public void testUpdateApprovalStatus_doctorNotFound() {
        // Arrange
        Integer doctorId = 1;
        Mockito.when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateApprovalStatus(doctorId, Doctors.ApprovalStatus.Approved));
    }

    @Test
    public void testUpdateApprovalStatus_invalidOperation() {
        // Arrange
        Doctors doctor = new Doctors();
        doctor.setApprovalStatus(Doctors.ApprovalStatus.Approved);
        Mockito.when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));

        // Act & Assert
        assertThrows(InvalidOperationException.class, () -> doctorService.updateApprovalStatus(1, Doctors.ApprovalStatus.Rejected));
    }

    @Test
    public void testGetAllDoctors_throwsException() {
        // Arrange
        Mockito.when(doctorRepository.findByApprovalStatus(Mockito.any()))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> doctorService.getAllDoctors(Doctors.ApprovalStatus.Approved));
    }

    @Test
    public void testUpdateApprovalStatus_throwsException() {
        // Arrange
        Integer doctorId = 1;
        Mockito.doThrow(new RuntimeException("Database error"))
                .when(doctorRepository).save(Mockito.any());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> doctorService.updateApprovalStatus(doctorId, Doctors.ApprovalStatus.Approved));
    }



}

