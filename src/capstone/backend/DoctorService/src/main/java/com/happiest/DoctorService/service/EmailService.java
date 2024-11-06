package com.happiest.DoctorService.service;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCancellationEmail(String patientEmail, String patientName, String appointmentDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(patientEmail);
        message.setSubject(Constants.CANCELLATION_EMAIL_SUBJECT);
        message.setText(String.format(Constants.CANCELLATION_EMAIL_BODY_TEMPLATE, patientName, appointmentDetails));

        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new EmailException(Constants.FAILED_TO_SEND_MAIL, e);
        }
    }
}
