package com.happiest.AdminService.service;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.exception.InvalidOperationException;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.repository.DoctorRepo;

import com.happiest.AdminService.utility.RBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.happiest.AdminService.model.Doctors.ApprovalStatus.Pending;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LogManager.getLogger(DoctorService.class);


    public List<DoctorDTO> getAllDoctors(Doctors.ApprovalStatus approvalStatus) {
        logger.info(RBundle.getKey(Constants.LOG_FETCH_DOCTORS_WITH_STATUS, approvalStatus));
        if (approvalStatus != null) {
            return doctorRepository.findByApprovalStatus(approvalStatus)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            logger.info(RBundle.getKey(Constants.LOG_FETCH_ALL_DOCTORS));
            return doctorRepository.findAll(Sort.by(Sort.Direction.DESC, Constants.ENTITY_DOCTOR_ID))
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<DoctorDTO> getPendingApprovals() {
        logger.info(RBundle.getKey(Constants.LOG_FETCH_PENDING_APPROVALS));
        return doctorRepository.findByApprovalStatus(Constants.APPROVAL_STATUS_PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Doctors> getDoctorsByApprovalStatus(Doctors.ApprovalStatus status) {
        logger.info(RBundle.getKey(Constants.LOG_FETCH_DOCTORS_WITH_STATUS, status));
        List<Doctors> doctors = doctorRepository.findByApprovalStatus(status);
        if (doctors.isEmpty()) {
            logger.warn(RBundle.getKey(Constants.LOG_DOCTOR_NOT_FOUND, status));
            throw new ResourceNotFoundException(String.format(RBundle.getKey(Constants.ERROR_NO_DOCTORS_FOUND, status)));
        }
        return doctors;
    }

    public void updateApprovalStatus(Integer doctorId, Doctors.ApprovalStatus status) {
        logger.info(RBundle.getKey(Constants.LOG_UPDATE_DOCTOR_STATUS, doctorId));
        Doctors doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    logger.error(RBundle.getKey(Constants.LOG_DOCTOR_NOT_FOUND, doctorId));
                    return new ResourceNotFoundException(String.format(RBundle.getKey(Constants.LOG_DOCTOR_NOT_FOUND, doctorId)));
                });

        if (doctor.getApprovalStatus() != Constants.APPROVAL_STATUS_PENDING) {
            logger.warn(RBundle.getKey(Constants.ERROR_INVALID_OPERATION, doctor.getApprovalStatus()));
            throw new InvalidOperationException(String.format(RBundle.getKey(Constants.ERROR_INVALID_OPERATION, doctor.getApprovalStatus())));
        }

        doctor.setApprovalStatus(status);
        doctorRepository.save(doctor);
        logger.info(RBundle.getKey(Constants.LOG_UPDATE_DOCTOR_STATUS, doctorId));

        // Send email notification
        String message = String.format(Constants.EMAIL_MESSAGE_DOCTOR_APPROVAL_TEMPLATE, doctor.getUser().getName(), status.name().toLowerCase());
        emailService.sendEmail(doctor.getUser().getEmail(), Constants.EMAIL_SUBJECT_DOCTOR_APPROVAL_STATUS, message);
        logger.info(RBundle.getKey(Constants.LOG_EMAIL_NOTIFICATION_SENT, doctorId));
    }

    private DoctorDTO convertToDTO(Doctors doctor) {
        return new DoctorDTO(
                doctor.getDoctorId(),
                doctor.getUser().getName(),
                doctor.getSpecialization(),
                doctor.getMedicalLicenseNumber(),
                doctor.getApprovalStatus(),
                doctor.getHospitalName(),
                doctor.getYearsOfExperience(),
                doctor.getUser().getEmail()
        );
    }

}

