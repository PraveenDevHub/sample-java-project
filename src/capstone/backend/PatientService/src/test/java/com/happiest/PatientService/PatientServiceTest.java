package com.happiest.PatientService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.happiest.PatientService.constants.Constants;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.dto.Users;
import com.happiest.PatientService.exceptions.FilterCriteriaException;
import com.happiest.PatientService.exceptions.PatientNotFoundException;
import com.happiest.PatientService.exceptions.ResourceNotFoundException;
import com.happiest.PatientService.repository.DoctorRepo;
import com.happiest.PatientService.repository.PatientRepo;
import com.happiest.PatientService.repository.UserRepo;
import com.happiest.PatientService.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PatientServiceTest {

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private PatientService patientService;

    private Doctors doctor;
    private Patients patient;
    private Users user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doctor = new Doctors();
        doctor.setApprovalStatus(Doctors.ApprovalStatus.Approved);
        doctor.setState("State");
        doctor.setCity("City");
        doctor.setSpecialization("Specialization");
        doctor.setHospitalName("Hospital");
        user = new Users();
        user.setName("Doctor Name");
        doctor.setUser(user);

        patient = new Patients();
        patient.setPatientId(1);
        patient.setAge(30);
        patient.setGender(Patients.Gender.Male); // Assuming Gender is an enum
        patient.setContact_number("1234567890");
        patient.setUser(user);
    }

    @Test
    void testGetDoctors_Success() {
        when(doctorRepo.findAll()).thenReturn(Arrays.asList(doctor));

        List<Doctors> doctors = patientService.getDoctors(null);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        verify(doctorRepo, times(1)).findAll();
    }

    @Test
    void testGetDoctors_WithLimit() {
        when(doctorRepo.findAll()).thenReturn(Arrays.asList(doctor));

        List<Doctors> doctors = patientService.getDoctors(1);

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        verify(doctorRepo, times(1)).findAll();
    }

    @Test
    void testGetDoctors_WithNegativeLimit() {
        when(doctorRepo.findAll()).thenReturn(Arrays.asList(doctor));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getDoctors(-1);
        });

        assertEquals(Constants.DOCTOR_NOT_FOUND, exception.getMessage());
        verify(doctorRepo, times(1)).findAll();
    }

    @Test
    void testFilterDoctors_Success() throws FilterCriteriaException {
        when(doctorRepo.findAll()).thenReturn(Arrays.asList(doctor));

        List<Doctors> doctors = patientService.filterDoctors("State", "City", "Specialization", "Hospital", "Doctor");

        assertNotNull(doctors);
        assertEquals(1, doctors.size());
        verify(doctorRepo, times(1)).findAll();
    }


    @Test
    void testFindById_Success() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient));

        Optional<Patients> foundPatient = patientService.findById(1);

        assertTrue(foundPatient.isPresent());
        assertEquals(patient, foundPatient.get());
        verify(patientRepo, times(1)).findById(anyInt());
    }

    @Test
    void testFindById_NotFound() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patientService.findById(1);
        });

        assertTrue(exception.getCause() instanceof PatientNotFoundException);
        assertEquals(Constants.PATIENT_NOT_FOUND + 1, exception.getCause().getMessage());
        verify(patientRepo, times(1)).findById(anyInt());
    }

    @Test
    void testUpdatePatientProfile_Success() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patient));
        when(userRepo.save(any(Users.class))).thenReturn(user);
        when(patientRepo.save(any(Patients.class))).thenReturn(patient);

        Patients updatedPatient = new Patients();
        updatedPatient.setAge(35);
        updatedPatient.setGender(Patients.Gender.Female); // Assuming Gender is an enum
        updatedPatient.setContact_number("0987654321");
        Users updatedUser = new Users();
        updatedUser.setName("Updated Name");
        updatedPatient.setUser(updatedUser);

        Patients result = patientService.updatePatientProfile(1, updatedPatient);

        assertNotNull(result);
        assertEquals(35, result.getAge());
        assertEquals(Patients.Gender.Female, result.getGender()); // Compare as enum
        assertEquals("0987654321", result.getContact_number());
        assertEquals("Updated Name", result.getUser().getName());
        verify(patientRepo, times(1)).findById(anyInt());
        verify(userRepo, times(1)).save(any(Users.class));
        verify(patientRepo, times(1)).save(any(Patients.class));
    }

    @Test
    void testUpdatePatientProfile_NotFound() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            patientService.updatePatientProfile(1, patient);
        });

        assertTrue(exception.getCause() instanceof PatientNotFoundException);
        assertEquals(Constants.PATIENT_NOT_FOUND + 1, exception.getCause().getMessage());
        verify(patientRepo, times(1)).findById(anyInt());
    }
}