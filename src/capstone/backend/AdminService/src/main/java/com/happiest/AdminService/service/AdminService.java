package com.happiest.AdminService.service;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Appointments;
import com.happiest.AdminService.repository.AppointmentRepository;
import com.happiest.AdminService.repository.DoctorRepo;
import com.happiest.AdminService.repository.PatientRepo;
import com.happiest.AdminService.repository.UserRepo;
import com.happiest.AdminService.utility.RBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.happiest.AdminService.model.Doctors.ApprovalStatus.Pending;

@Service
public class AdminService {

    @Autowired
    private PatientRepo patientRepository;

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private static final Logger logger = LogManager.getLogger(AdminService.class);


    public Map<String, Long> getDashboardCounts() {
        try {
            // Fetch the log message from constant.properties
            logger.info(RBundle.getKey(Constants.LOG_FETCH_DASHBOARD_COUNTS));

            // Prepare the map of counts
            Map<String, Long> counts = new HashMap<>();
            counts.put(Constants.ENTITY_USERS, userRepository.count());
            counts.put(Constants.ENTITY_PATIENTS, patientRepository.count());
            counts.put(Constants.ENTITY_DOCTORS, doctorRepository.count());
            counts.put(Constants.ENTITY_APPOINTMENTS, appointmentRepository.count());

            // Count pending approvals using the approval status constant
            counts.put(Constants.ENTITY_APPROVALS, doctorRepository.countByApprovalStatus(Constants.APPROVAL_STATUS_PENDING));

            // Log success message from constant.properties
            logger.info(RBundle.getKey(Constants.LOG_FETCH_DASHBOARD_SUCCESS));

            return counts;
        } catch (Exception e) {
            // Log error message from constant.properties
            logger.error(RBundle.getKey(Constants.LOG_FETCH_DASHBOARD_FAILURE));

            // Throw exception with a message from constant.properties
            throw new ResourceNotFoundException(RBundle.getKey(Constants.ERROR_FETCH_DASHBOARD_COUNTS));
        }
    }


    public Map<String, Object> getAppointmentStatistics() {
        List<Appointments> appointments = appointmentRepository.findAll();
        Map<String, Object> statistics = new HashMap<>();

        // Process daily, weekly, and monthly statistics
        Map<LocalDate, Long> dailyAppointments = appointments.stream()
                .collect(Collectors.groupingBy(Appointments::getAppointmentDate, Collectors.counting()));

//        Map<YearWeek, Long> weeklyAppointments = appointments.stream()
//                .collect(Collectors.groupingBy(a -> YearWeek.from(a.getAppointmentDate()), Collectors.counting()));

        Map<YearMonth, Long> monthlyAppointments = appointments.stream()
                .collect(Collectors.groupingBy(a -> YearMonth.from(a.getAppointmentDate()), Collectors.counting()));

        statistics.put("daily", dailyAppointments);
//        statistics.put("weekly", weeklyAppointments);
        statistics.put("monthly", monthlyAppointments);

        return statistics;
    }


}

