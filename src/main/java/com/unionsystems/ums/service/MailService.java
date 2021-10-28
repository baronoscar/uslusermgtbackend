package com.unionsystems.ums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    void sendOTP(String token, String email, String name) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Email OTP for Verification ");
        msg.setText("Hello " + name + ", \n Please verify your email with this OTP: " + token);

        mailSender.send(msg);
    }

    public void sendPassword(String plainPass, String email, String firstName) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Email OTP for Verification ");
        msg.setText("Hello " + firstName + ", \n Please login with your email and this password: " + plainPass);

//        mailSender.send(msg);
    }
}
