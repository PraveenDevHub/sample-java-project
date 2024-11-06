package com.happiest.DoctorService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.dto.Users;
import com.happiest.DoctorService.exception.DoctorNotFoundException;
import com.happiest.DoctorService.exception.FileStorageException;
import com.happiest.DoctorService.repository.DoctorRepo;
import com.happiest.DoctorService.repository.UserRepo;
import com.happiest.DoctorService.response.UploadFileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private DoctorService doctorService;

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
    void testFindByIdSuccess() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));

        Optional<Doctors> foundDoctor = doctorService.findById(1);

        assertTrue(foundDoctor.isPresent());
        assertEquals(1, foundDoctor.get().getDoctorId());
        verify(doctorRepo, times(1)).findById(anyInt());
    }

    @Test
    void testFindByIdFailure() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doctorService.findById(1);
        });

        assertTrue(exception.getCause() instanceof DoctorNotFoundException);
        assertEquals(String.format(Constants.DOCTOR_NOT_FOUND_WITH_ID, 1), exception.getCause().getMessage());
        verify(doctorRepo, times(1)).findById(anyInt());
    }

    @Test
    void testUpdateDoctorProfileSuccess() throws FileStorageException {
        MultipartFile profilePhoto = mock(MultipartFile.class);
        UploadFileResponse uploadFileResponse = new UploadFileResponse("fileName", "fileDownloadUri", "fileType", 12345);

        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(uploadFileResponse);
        when(doctorRepo.save(any(Doctors.class))).thenReturn(doctor);

        Doctors updatedDoctor = doctorService.updateDoctorProfile(1, profilePhoto, "Dr. John Doe", "Cardiology", 10, "Experienced cardiologist", "City Hospital", "California", "Los Angeles");

        assertNotNull(updatedDoctor);
        assertEquals("fileDownloadUri", updatedDoctor.getProfilePhoto());
        assertEquals("Dr. John Doe", updatedDoctor.getUser().getName());
        verify(doctorRepo, times(1)).findById(anyInt());
        verify(fileStorageService, times(1)).storeFile(any(MultipartFile.class));
        verify(userRepo, times(1)).save(any(Users.class));
        verify(doctorRepo, times(1)).save(any(Doctors.class));
    }

    @Test
    void testUpdateDoctorProfileFailure() {
        when(doctorRepo.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doctorService.updateDoctorProfile(1, null, "Dr. John Doe", "Cardiology", 10, "Experienced cardiologist", "City Hospital", "California", "Los Angeles");
        });

        assertTrue(exception.getCause() instanceof DoctorNotFoundException);
        assertEquals(Constants.DOCTOR_NOT_FOUND_EXCEPTION, exception.getCause().getMessage());
        verify(doctorRepo, times(1)).findById(anyInt());
    }

    @Test
    void testUpdateDoctorProfileFileStorageFailure() throws FileStorageException {
        MultipartFile profilePhoto = mock(MultipartFile.class);

        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));
        doThrow(new FileStorageException("File storage exception")).when(fileStorageService).storeFile(any(MultipartFile.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doctorService.updateDoctorProfile(1, profilePhoto, "Dr. John Doe", "Cardiology", 10, "Experienced cardiologist", "City Hospital", "California", "Los Angeles");
        });

        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertEquals("File storage exception", cause.getMessage());
        verify(doctorRepo, times(1)).findById(anyInt());
        verify(fileStorageService, times(1)).storeFile(any(MultipartFile.class));
    }


    @Test
    void testUpdateDoctorProfileSaveFailure() throws FileStorageException {
        MultipartFile profilePhoto = mock(MultipartFile.class);
        UploadFileResponse uploadFileResponse = new UploadFileResponse("fileName", "fileDownloadUri", "fileType", 12345);

        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));
        when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn(uploadFileResponse);
        doThrow(new RuntimeException("Save exception")).when(doctorRepo).save(any(Doctors.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doctorService.updateDoctorProfile(1, profilePhoto, "Dr. John Doe", "Cardiology", 10, "Experienced cardiologist", "City Hospital", "California", "Los Angeles");
        });

        assertEquals(Constants.CANNOT_SAVE_DOCTOR_PROFILE, exception.getMessage());
        verify(doctorRepo, times(1)).findById(anyInt());
        verify(fileStorageService, times(1)).storeFile(any(MultipartFile.class));
        verify(userRepo, times(1)).save(any(Users.class));
        verify(doctorRepo, times(1)).save(any(Doctors.class));
    }
}

