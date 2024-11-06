package com.happiest.AdminService.service;

import com.happiest.AdminService.constants.Constants;
import com.happiest.AdminService.exception.EmailSendException;
import com.happiest.AdminService.utility.RBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LogManager.getLogger(EmailService.class);


    public void sendEmail(String to, String subject, String body) {
        try {
            // Logging the start of email sending
            logger.info(RBundle.getKey(Constants.EMAIL_SEND_START));

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            // Logging successful email sending
            logger.info(RBundle.getKey(Constants.EMAIL_SEND_SUCCESS));
        } catch (Exception e) {
            // Logging email sending failure
            logger.error(RBundle.getKey(Constants.EMAIL_SEND_FAILURE));

            // Throwing a custom exception with a detailed message
            throw new EmailSendException(RBundle.getKey(Constants.EMAIL_SEND_ERROR));
        }
    }

}


