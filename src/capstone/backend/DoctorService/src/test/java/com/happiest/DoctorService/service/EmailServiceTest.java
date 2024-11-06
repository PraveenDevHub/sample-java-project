package com.happiest.DoctorService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.exception.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private SimpleMailMessage message;

    @BeforeEach
    void setUp() {
        message = new SimpleMailMessage();
        message.setTo("patient@example.com");
        message.setSubject("Appointment Cancellation");
        message.setText("Dear Patient, your appointment has been cancelled.");
    }

    @Test
    void testSendCancellationEmailSuccess() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendCancellationEmail("patient@example.com", "Patient", "Appointment details");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendCancellationEmailFailure() {
        doThrow(new MailException("Mail sending failed") {}).when(mailSender).send(any(SimpleMailMessage.class));

        Exception exception = assertThrows(EmailException.class, () -> {
            emailService.sendCancellationEmail("patient@example.com", "Patient", "Appointment details");
        });

        assertEquals(Constants.FAILED_TO_SEND_MAIL, exception.getMessage());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

