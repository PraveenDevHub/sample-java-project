package com.happiest.AdminService;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.dto.PatientDTO;
import com.happiest.AdminService.exception.EmailSendException;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.model.Patients;
import com.happiest.AdminService.model.Users;
import com.happiest.AdminService.repository.AppointmentRepository;
import com.happiest.AdminService.repository.DoctorRepo;
import com.happiest.AdminService.repository.PatientRepo;
import com.happiest.AdminService.repository.UserRepo;
import com.happiest.AdminService.service.AdminService;
import com.happiest.AdminService.service.DoctorService;
import com.happiest.AdminService.service.EmailService;
import com.happiest.AdminService.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @MockBean
    private PatientRepo patientRepository;

    @Test
    public void testGetAllPatients_success() {
        // Arrange
        Users user = new Users();
        user.setName("Patient Name");
        user.setEmail("patient@example.com");

        Patients patient = new Patients();
        patient.setPatientId(1);
        patient.setUser(user); // Set the user for the patient

        List<Patients> patientsList = Arrays.asList(patient);

        Mockito.when(patientRepository.findAll(Sort.by(Sort.Direction.DESC, "patientId")))
                .thenReturn(patientsList);

        // Act
        List<PatientDTO> result = patientService.getAllPatients();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals("Patient Name", result.get(0).getName()); // Additional check
    }

    @Test
    public void testGetAllPatients_failure() {
        // Arrange
        Mockito.when(patientRepository.findAll(Sort.by(Sort.Direction.DESC, "patientId")))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> patientService.getAllPatients());
    }

    @Test
    public void testGetAllPatients_noPatientsFound() {
        // Arrange
        Mockito.when(patientRepository.findAll(Sort.by(Sort.Direction.DESC, "patientId"))).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> patientService.getAllPatients());
    }

    @Test
    public void testGetAllPatients_throwsException() {
        // Arrange
        Mockito.when(patientRepository.findAll((Example<Patients>) Mockito.any()))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> patientService.getAllPatients());
    }


}

