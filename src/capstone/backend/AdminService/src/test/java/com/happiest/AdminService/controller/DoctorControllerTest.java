package com.happiest.AdminService.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.service.DoctorService;
//import com.happiest.AdminService.model.DoctorDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

class DoctorControllerTest {

    @InjectMocks
    private DoctorController doctorController;

    @Mock
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDoctors_withStatus() {
        // Arrange
        List<DoctorDTO> doctorsList = List.of(new DoctorDTO());
        when(doctorService.getAllDoctors(any())).thenReturn(doctorsList);

        // Act
        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctors(null);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(doctorsList, response.getBody());
    }

    @Test
    void testGetAllDoctors_withoutStatus() {
        // Arrange
        List<DoctorDTO> doctorsList = List.of(new DoctorDTO());
        when(doctorService.getAllDoctors(null)).thenReturn(doctorsList);

        // Act
        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctors(null);

        // Assert
        assertEquals(OK, response.getStatusCode());
        assertEquals(doctorsList, response.getBody());
    }

//    @Test
//    public void testGetAllDoctors_throwsException() {
//        // Arrange
//        Mockito.when(doctorService.getAllDoctors(Mockito.any())).thenThrow(new RuntimeException("Service error"));
//
//        // Act
//        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctors(null);
//
//        // Assert
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }


}

