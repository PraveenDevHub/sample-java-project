package com.happiest.PatientService.service;

import com.happiest.PatientService.constants.Constants;
import com.happiest.PatientService.dto.Doctors;
import com.happiest.PatientService.dto.Patients;
import com.happiest.PatientService.dto.Users;
import com.happiest.PatientService.exceptions.DoctorNotFoundException;
import com.happiest.PatientService.exceptions.FilterCriteriaException;
import com.happiest.PatientService.exceptions.PatientNotFoundException;
import com.happiest.PatientService.exceptions.ResourceNotFoundException;
import com.happiest.PatientService.repository.DoctorRepo;
import com.happiest.PatientService.repository.PatientRepo;
import com.happiest.PatientService.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class PatientService {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private UserRepo userRepo;

    private static final Logger logger = LogManager.getLogger(PatientService.class.getName());

    // Fetch doctors with optional limit, filtering only approved doctors
    public List<Doctors> getDoctors(Integer limit) {
        List<Doctors> doctors = doctorRepo.findAll();
        logger.info(Constants.FETCHED_ALL_DOCTORS, doctors);

        doctors = doctors.stream()
                .filter(doctor -> Constants.APPROVED.equals(doctor.getApprovalStatus().name()))
                .collect(Collectors.toList());
        logger.info(Constants.FETCHED_APPROVED_DOCTORS, doctors);

        if (limit != null && limit < 0) {
            try {
                throw new DoctorNotFoundException(Constants.DOCTOR_NOT_FOUND);
            } catch (DoctorNotFoundException e) {
                throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
            }
        }
        if (limit != null && limit > 0) {
            return doctors.stream().limit(limit).collect(Collectors.toList());
        }
        return doctors;
    }

    // Filter doctors by various criteria
    public List<Doctors> filterDoctors(String state, String city, String specialization, String hospital, String searchTerm)  {
        List<Doctors> doctors = doctorRepo.findAll();
        logger.info(Constants.FETCHED_ALL_DOCTORS, doctors);

        doctors = doctors.stream()
                .filter(doctor -> Constants.APPROVED.equals(doctor.getApprovalStatus().name()))
                .collect(Collectors.toList());
        logger.info(Constants.FETCHED_APPROVED_DOCTORS, doctors);

        if (state != null && !state.isEmpty()) {
            doctors = doctors.stream().filter(doctor -> state.equals(doctor.getState())).collect(Collectors.toList());
        }
        if (city != null && !city.isEmpty()) {
            doctors = doctors.stream().filter(doctor -> city.equals(doctor.getCity())).collect(Collectors.toList());
        }
        if (specialization != null && !specialization.isEmpty()) {
            doctors = doctors.stream().filter(doctor -> specialization.equals(doctor.getSpecialization())).collect(Collectors.toList());
        }
        if (hospital != null && !hospital.isEmpty()) {
            doctors = doctors.stream().filter(doctor -> hospital.equals(doctor.getHospitalName())).collect(Collectors.toList());
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            String lowerCaseSearchTerm = searchTerm.toLowerCase();
            doctors = doctors.stream().filter(doctor ->
                    doctor.getUser().getName().toLowerCase().startsWith(lowerCaseSearchTerm) ||
                            doctor.getSpecialization().toLowerCase().startsWith(lowerCaseSearchTerm)
            ).collect(Collectors.toList());
        }
//        else {
//            throw new FilterCriteriaException("No Filters applicable");
//        }

        logger.info(Constants.FILTERED_DOCTORS, doctors);
        return doctors;
    }

    // Find patient by ID
    public Optional<Patients> findById(Integer patientId)  {
        logger.info(Constants.FETCHING_PATIENT, patientId);
        try {
            return Optional.ofNullable(patientRepo.findById(patientId)
                    .orElseThrow(() -> new PatientNotFoundException(Constants.PATIENT_NOT_FOUND + patientId)));
        } catch (PatientNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Update patient profile
    public Patients updatePatientProfile(Integer patientId, Patients updatedPatient) {
        logger.info(Constants.UPDATING_PATIENT_PROFILE, patientId);

        Patients patient = null;
        try {
            patient = patientRepo.findById(patientId)
                    .orElseThrow(() -> new PatientNotFoundException(Constants.PATIENT_NOT_FOUND + patientId));
        } catch (PatientNotFoundException e) {
            throw new RuntimeException(e);
        }

        Users user = patient.getUser();
        user.setName(updatedPatient.getUser().getName());
        userRepo.save(user);
        logger.info(Constants.UPDATED_USER_DETAILS, patientId);

        patient.setAge(updatedPatient.getAge());
        patient.setGender(updatedPatient.getGender());
        patient.setContact_number(updatedPatient.getContact_number());

        Patients savedPatient = patientRepo.save(patient);
        logger.info(Constants.UPDATED_PATIENT_PROFILE, patientId);

        return savedPatient;
    }

}
