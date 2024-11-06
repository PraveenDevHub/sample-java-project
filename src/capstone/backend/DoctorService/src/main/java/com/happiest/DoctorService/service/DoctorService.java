package com.happiest.DoctorService.service;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.dto.Users;
import com.happiest.DoctorService.exception.DoctorNotFoundException;
import com.happiest.DoctorService.exception.FileStorageException;
import com.happiest.DoctorService.repository.DoctorRepo;
import com.happiest.DoctorService.repository.UserRepo;
import com.happiest.DoctorService.response.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepo userRepo;

    public Optional<Doctors> findById(Integer doctorId) {
        return doctorRepo.findById(doctorId)
                .or(() -> {
                    try {
                        throw new DoctorNotFoundException(String.format(Constants.DOCTOR_NOT_FOUND_WITH_ID, doctorId));
                    } catch (DoctorNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public Doctors updateDoctorProfile(Integer doctorId, MultipartFile profilePhoto, String name, String specialization, Integer yearsOfExperience, String doctorDescription, String hospitalName, String state, String city) {
        Doctors doctor = null;
        try {
            doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(Constants.DOCTOR_NOT_FOUND_EXCEPTION));
        } catch (DoctorNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            // Store the profile photo and get the URL
            UploadFileResponse uploadFileResponse = null;
            try {
                uploadFileResponse = fileStorageService.storeFile(profilePhoto);
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
            String profilePhotoUrl = uploadFileResponse.getFileDownloadUri();
            doctor.setProfilePhoto(profilePhotoUrl);
        }
        Users user = doctor.getUser();
        user.setName(name);
        userRepo.save(user);

        // Update other doctor details
        doctor.setSpecialization(specialization);
        doctor.setYearsOfExperience(yearsOfExperience);
        doctor.setDoctorDescription(doctorDescription);
        doctor.setHospitalName(hospitalName);
        doctor.setState(state);
        doctor.setCity(city);
        // Save the updated doctor information
        try {
            return doctorRepo.save(doctor);
        } catch (Exception e) {
            throw new RuntimeException(Constants.CANNOT_SAVE_DOCTOR_PROFILE, e);
        }
    }

}
