package com.happiest.DoctorService.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.happiest.DoctorService.dto.Doctors;
import com.happiest.DoctorService.exception.DuplicateScheduleException;
import com.happiest.DoctorService.exception.ResourceNotFoundException;
import com.happiest.DoctorService.model.DefaultSchedule;
import com.happiest.DoctorService.model.DoctorProfile;
import com.happiest.DoctorService.repository.DefaultScheduleRepository;
import com.happiest.DoctorService.repository.DoctorProfileRepository;
import com.happiest.DoctorService.repository.DoctorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DoctorProfileServiceTest {

    @Mock
    private DoctorProfileRepository doctorProfileRepository;

    @Mock
    private DefaultScheduleRepository defaultScheduleRepository;

    @Mock
    private DoctorRepo doctorRepo;

    @InjectMocks
    private DoctorProfileService doctorProfileService;

    private DoctorProfile doctorProfile;
    private Doctors doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctors();
        doctor.setDoctorId(1);

        doctorProfile = new DoctorProfile();
        doctorProfile.setDoctor(doctor);
        doctorProfile.setAvailableDate(LocalDate.now());
        doctorProfile.setTimeBlockStart(LocalTime.of(9, 0));
        doctorProfile.setTimeBlockEnd(LocalTime.of(17, 0));
    }

    @Test
    void testCreateDoctorProfile_Success() {
        when(doctorProfileRepository.findByDoctor_DoctorIdAndAvailableDateAndTimeBlockStartAndTimeBlockEnd(
                anyInt(), any(), any(), any())).thenReturn(Collections.emptyList());
        when(doctorProfileRepository.save(any(DoctorProfile.class))).thenReturn(doctorProfile);

        DoctorProfile result = doctorProfileService.createDoctorProfile(doctorProfile);

        assertNotNull(result);
        assertEquals(doctorProfile, result);
        verify(doctorProfileRepository, times(1)).save(doctorProfile);
    }

    @Test
    void testCreateDoctorProfile_DuplicateSchedule() {
        when(doctorProfileRepository.findByDoctor_DoctorIdAndAvailableDateAndTimeBlockStartAndTimeBlockEnd(
                anyInt(), any(), any(), any())).thenReturn(Collections.singletonList(doctorProfile));

        assertThrows(DuplicateScheduleException.class, () -> {
            doctorProfileService.createDoctorProfile(doctorProfile);
        });

        verify(doctorProfileRepository, never()).save(any(DoctorProfile.class));
    }

    @Test
    void testGetAllDoctorProfiles_Success() {
        List<DoctorProfile> profiles = Arrays.asList(doctorProfile);
        when(doctorProfileRepository.findAllDoctorProfiles()).thenReturn(profiles);

        List<DoctorProfile> result = doctorProfileService.getAllDoctorProfiles();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(profiles, result);
    }

    @Test
    void testGetAllDoctorProfiles_NoProfilesFound() {
        when(doctorProfileRepository.findAllDoctorProfiles()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            doctorProfileService.getAllDoctorProfiles();
        });
    }

    @Test
    void testSaveDefaultSchedule_Success() {
        Map<String, Object> defaultSchedule = new HashMap<>();
        defaultSchedule.put("doctorId", 1);
        Map<String, List<Map<String, Object>>> scheduleMap = new HashMap<>();
        List<Map<String, Object>> timeBlocks = new ArrayList<>();
        Map<String, Object> timeBlock = new HashMap<>();
        timeBlock.put("start", "09:00");
        timeBlock.put("end", "17:00");
        timeBlock.put("duration", "30");
        timeBlock.put("availableTimeSlots", Arrays.asList("09:00-09:30", "09:30-10:00"));
        timeBlocks.add(timeBlock);
        scheduleMap.put("Monday", timeBlocks);
        defaultSchedule.put("defaultSchedule", scheduleMap);

        when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctor));

        assertDoesNotThrow(() -> {
            doctorProfileService.saveDefaultSchedule(defaultSchedule);
        });

        verify(defaultScheduleRepository, times(1)).deleteByDoctor_DoctorIdAndDayOfWeek(anyInt(), anyString());
        verify(defaultScheduleRepository, times(1)).save(any(DefaultSchedule.class));
    }

    @Test
    void testSaveDefaultSchedule_DoctorNotFound() {
        Map<String, Object> defaultSchedule = new HashMap<>();
        defaultSchedule.put("doctorId", 1);

        when(doctorRepo.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            doctorProfileService.saveDefaultSchedule(defaultSchedule);
        });

        verify(defaultScheduleRepository, never()).save(any(DefaultSchedule.class));
    }

    @Test
    void testGetDefaultScheduleForDay_Success() {
        List<DefaultSchedule> schedules = Arrays.asList(new DefaultSchedule());
        when(defaultScheduleRepository.findByDoctor_DoctorIdAndDayOfWeek(anyInt(), anyString())).thenReturn(schedules);

        List<DefaultSchedule> result = doctorProfileService.getDefaultScheduleForDay(1, "Monday");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(schedules, result);
    }

    @Test
    void testGetDefaultScheduleForDay_NoSchedulesFound() {
        when(defaultScheduleRepository.findByDoctor_DoctorIdAndDayOfWeek(anyInt(), anyString())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            doctorProfileService.getDefaultScheduleForDay(1, "Monday");
        });
    }
}

