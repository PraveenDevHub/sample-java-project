package com.happiest.APIGatewayJWT2.service;

import com.happiest.APIGatewayJWT2.exception.EmailSendingException;
import com.happiest.APIGatewayJWT2.constants.EmailConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException(EmailConstants.EMAIL_SENDING_FAILURE + to, e);
        }
    }

    public void sendVerificationEmail(String email, String token) {
        try {
            String verificationLink = EmailConstants.VERIFICATION_URL + token;
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(EmailConstants.EMAIL_VERIFICATION_SUBJECT);
            helper.setText(String.format(EmailConstants.EMAIL_VERIFICATION_TEXT, verificationLink), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(EmailConstants.EMAIL_VERIFICATION_FAILURE + email, e);
        }
    }

    public void sendPasswordResetEmail(String email, String token) {
        try {
            String resetLink = EmailConstants.PASSWORD_RESET_URL + token;
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(EmailConstants.PASSWORD_RESET_SUBJECT);
            helper.setText(String.format(EmailConstants.PASSWORD_RESET_TEXT, resetLink), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(EmailConstants.PASSWORD_RESET_FAILURE + email, e);
        }
    }
}
