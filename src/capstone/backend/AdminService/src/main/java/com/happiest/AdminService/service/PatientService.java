package com.happiest.AdminService.service;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.PatientDTO;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Patients;
import com.happiest.AdminService.repository.PatientRepo;
import com.happiest.AdminService.utility.RBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepo patientRepository;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(PatientService.class);


    public List<PatientDTO> getAllPatients() {
        logger.info(RBundle.getKey(Constants.PATIENT_FETCH_SUCCESS));
        List<Patients> patients = patientRepository.findAll(Sort.by(Sort.Direction.DESC, "patientId"));

        if (patients.isEmpty()) {
            logger.warn(RBundle.getKey(Constants.PATIENT_FETCH_FAILURE));
            throw new ResourceNotFoundException(RBundle.getKey(Constants.PATIENT_FETCH_FAILURE));
        }

        return patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PatientDTO convertToDTO(Patients patient) {
        return new PatientDTO(
                patient.getPatientId(),
                patient.getUser().getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getUser().getEmail()
        );
    }

}

