package com.happiest.AdminService;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.dto.DoctorDTO;
import com.happiest.AdminService.exception.EmailSendException;
import com.happiest.AdminService.exception.ResourceNotFoundException;
import com.happiest.AdminService.model.Doctors;
import com.happiest.AdminService.model.Users;
import com.happiest.AdminService.repository.AppointmentRepository;
import com.happiest.AdminService.repository.DoctorRepo;
import com.happiest.AdminService.repository.PatientRepo;
import com.happiest.AdminService.repository.UserRepo;
import com.happiest.AdminService.service.AdminService;
import com.happiest.AdminService.service.DoctorService;
import com.happiest.AdminService.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void testSendEmail_success() {
        // Arrange
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("test@example.com");
        message.setSubject("Test Subject");
        message.setText("Test Body");

        Mockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail("test@example.com", "Test Subject", "Test Body");

        // Assert
        Mockito.verify(mailSender, Mockito.times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmail_failure() {
        // Arrange
        Mockito.doThrow(new MailException("Email send failed") {}).when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        assertThrows(EmailSendException.class, () -> emailService.sendEmail("test@example.com", "Test Subject", "Test Body"));
    }

    @Test
    public void testSendEmail_throwsException() {
        // Arrange
        Mockito.doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        assertThrows(EmailSendException.class, () -> emailService.sendEmail("test@example.com", "Subject", "Body"));
    }


}

