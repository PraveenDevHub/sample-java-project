package com.happiest.DoctorService.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.dto.Users;
import com.happiest.DoctorService.exception.DoctorNotFoundException;
import com.happiest.DoctorService.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private Doctors doctor;
    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setName("Dr. John Doe");

        doctor = new Doctors();
        doctor.setDoctorId(1);
        doctor.setUser(user);
    }

    @Test
    void testGreet() {
        String response = doctorController.greet();
        assertEquals("This is Doctor Service", response);
    }

    @Test
    void testGetDoctorByIdSuccess() {
        when(doctorService.findById(anyInt())).thenReturn(Optional.of(doctor));

        ResponseEntity<Doctors> response = doctorController.getDoctorById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
        verify(doctorService, times(1)).findById(anyInt());
    }

    @Test
    void testGetDoctorByIdFailure() {
        when(doctorService.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doctorController.getDoctorById(1);
        });

        assertTrue(exception.getCause() instanceof DoctorNotFoundException);
        assertEquals(Constants.DOCTOR_NOT_FOUND_WITH_ID + 1, exception.getCause().getMessage());
        verify(doctorService, times(1)).findById(anyInt());
    }

    @Test
    void testUpdateDoctorProfileSuccess() {
        MultipartFile profilePhoto = mock(MultipartFile.class);
        when(doctorService.updateDoctorProfile(anyInt(), any(MultipartFile.class), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(doctor);

        ResponseEntity<Doctors> response = doctorController.updateDoctorProfile(1, profilePhoto, "Dr. John Doe", "Cardiology", 10, "Experienced cardiologist", "City Hospital", "California", "Los Angeles");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctor, response.getBody());
        verify(doctorService, times(1)).updateDoctorProfile(anyInt(), any(MultipartFile.class), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateDoctorProfileFailure() {
        MultipartFile profilePhoto = mock(MultipartFile.class);
        when(doctorService.updateDoctorProfile(anyInt(), any(MultipartFile.class), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException(Constants.CANNOT_SAVE_DOCTOR_PROFILE));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doctorController.updateDoctorProfile(1, profilePhoto, "Dr. John Doe", "Cardiology", 10, "Experienced cardiologist", "City Hospital", "California", "Los Angeles");
        });

        assertEquals(Constants.CANNOT_SAVE_DOCTOR_PROFILE, exception.getMessage());
        verify(doctorService, times(1)).updateDoctorProfile(anyInt(), any(MultipartFile.class), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString());
    }
}

