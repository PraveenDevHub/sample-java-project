package com.happiest.PatientService;

import com.happiest.PatientService.controller.PatientController;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.dto.Users;
import com.happiest.PatientService.exceptions.DoctorNotFoundException;
import com.happiest.PatientService.exceptions.PatientNotFoundException;
import com.happiest.PatientService.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private List<Doctors> doctorsList;
    private Patients patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Doctors doctor1 = new Doctors();
        doctor1.setApprovalStatus(Doctors.ApprovalStatus.Approved);

        Doctors doctor2 = new Doctors();
        doctor2.setApprovalStatus(Doctors.ApprovalStatus.Pending);

        Doctors doctor3 = new Doctors();
        doctor3.setApprovalStatus(Doctors.ApprovalStatus.Approved);

        doctorsList = Arrays.asList(doctor1, doctor2, doctor3);

        patient = new Patients();
        patient.setPatientId(1);
        patient.setAge(30);
        patient.setGender(Patients.Gender.Male);
        patient.setContact_number("1234567890");

        Users user = new Users();
        user.setName("John Doe");
        patient.setUser(user);
    }

    @Test
    void testGetDoctorsWithoutLimit() throws DoctorNotFoundException {
        when(patientService.getDoctors(null)).thenReturn(doctorsList);

        ResponseEntity<List<Doctors>> response = patientController.getDoctors(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
        verify(patientService, times(1)).getDoctors(null);
    }

    @Test
    void testGetDoctorsWithLimit() throws DoctorNotFoundException {
        when(patientService.getDoctors(1)).thenReturn(doctorsList.subList(0, 1));

        ResponseEntity<List<Doctors>> response = patientController.getDoctors(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getDoctors(1);
    }

    @Test
    void testFilterDoctorsByState() {
        when(patientService.filterDoctors("Karnataka", null, null, null, null)).thenReturn(doctorsList);

        ResponseEntity<List<Doctors>> response = patientController.filterDoctors("Karnataka", null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
        verify(patientService, times(1)).filterDoctors("Karnataka", null, null, null, null);
    }

    @Test
    void testGetPatientProfileSuccess() throws PatientNotFoundException {
        when(patientService.findById(1)).thenReturn(Optional.of(patient));

        ResponseEntity<Patients> response = patientController.getPatientProfile(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).findById(1);
    }

    @Test
    void testGetPatientProfileNotFound() throws PatientNotFoundException {
        when(patientService.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Patients> response = patientController.getPatientProfile(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(patientService, times(1)).findById(1);
    }

    @Test
    void testUpdatePatientProfileSuccess() throws  PatientNotFoundException {
        when(patientService.updatePatientProfile(1, patient)).thenReturn(patient);

        ResponseEntity<Patients> response = patientController.updatePatientProfile(1, patient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
        verify(patientService, times(1)).updatePatientProfile(1, patient);
    }

}
