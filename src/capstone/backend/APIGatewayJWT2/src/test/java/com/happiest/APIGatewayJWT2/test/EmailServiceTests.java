package com.happiest.APIGatewayJWT2.test;

import com.happiest.APIGatewayJWT2.constants.EmailConstants;
import com.happiest.APIGatewayJWT2.exception.EmailSendingException;
import com.happiest.APIGatewayJWT2.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTests {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail_Success() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendEmail("test@example.com", "Test Subject", "Test Text"));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmail_Failure() {
        doThrow(new RuntimeException("Mail sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        EmailSendingException exception = assertThrows(EmailSendingException.class, () ->
                emailService.sendEmail("test@example.com", "Test Subject", "Test Text")
        );
        assertEquals(EmailConstants.EMAIL_SENDING_FAILURE + "test@example.com", exception.getMessage());
    }

    @Test
    public void testSendVerificationEmail_Success() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendVerificationEmail("test@example.com", "token"));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendVerificationEmail_Failure() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Use a workaround to throw a checked exception
        doAnswer(invocation -> { throw new MessagingException("Mail sending failed"); })
                .when(mailSender).send(any(MimeMessage.class));

        EmailSendingException exception = assertThrows(EmailSendingException.class, () ->
                emailService.sendVerificationEmail("test@example.com", "token")
        );
        assertEquals(EmailConstants.EMAIL_VERIFICATION_FAILURE + "test@example.com", exception.getMessage());
    }

    @Test
    public void testSendPasswordResetEmail_Success() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail("test@example.com", "token"));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendPasswordResetEmail_Failure() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Use a workaround to throw a checked exception
        doAnswer(invocation -> { throw new MessagingException("Mail sending failed"); })
                .when(mailSender).send(any(MimeMessage.class));

        EmailSendingException exception = assertThrows(EmailSendingException.class, () ->
                emailService.sendPasswordResetEmail("test@example.com", "token")
        );
        assertEquals(EmailConstants.PASSWORD_RESET_FAILURE + "test@example.com", exception.getMessage());
    }
}
