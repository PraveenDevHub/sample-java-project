package com.happiest.AdminService.controller;

import static com.happiest.AdminService.constants.Constants.PATIENT_FETCH_FAILURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import com.happiest.AdminService.dto.PatientDTO;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.service.PatientService;
//import com.happiest.AdminService.model.PatientDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPatients() {
        // Arrange
        List<PatientDTO> patientsList = List.of(new PatientDTO());
        when(patientService.getAllPatients()).thenReturn(patientsList);

        // Act
        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(patientsList, response.getBody());
    }

//    @Test
//    public void testGetAllPatients_throwsException() {
//        // Arrange
//        Mockito.when(patientService.getAllPatients()).thenThrow(new ResourceNotFoundException(PATIENT_FETCH_FAILURE));
//
//        // Act
//        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();
//
//        // Assert
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }


}

