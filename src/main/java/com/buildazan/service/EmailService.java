package com.buildazan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService{

    // @Autowired
    // private JavaMailSender mailSender;

    
    public boolean sendVerificationLinkEmail(String toEmail, String subject, String verificationCode) {
        try {
            // MimeMessage message = mailSender.createMimeMessage();
            // MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // helper.setTo(toEmail);
            // helper.setSubject(subject);
            // String htmlBody = "<html><body><h2>Verification Link</h2><p>Your verification link is: <a href=\"https://buildazaan.netlify.app/verify-link?link="
            //         + verificationCode
            //         + "\">Click here to verify</a></p><p>Do not share it with anyone.</p><p>Note: Link will expire in 10 minutes</p></body></html>";
            // helper.setText(htmlBody, true);
            // mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    // Send OTP email
    public boolean sendOTPEmail(String toEmail, String subject, String otp) {
        try {
            // MimeMessage message = mailSender.createMimeMessage();
            // MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // helper.setTo(toEmail);
            // helper.setSubject(subject);
            // String htmlBody = "<html><body><h2>Your One-Time Password (OTP)</h2>"
            //         + "<p>Your OTP is: <strong>" + otp + "</strong></p>"
            //         + "<p>Please use this code within 10 minutes. Do not share it with anyone.</p>"
            //         + "<p>Note: OTP will expire in 10 minutes.</p></body></html>";
            // helper.setText(htmlBody, true);
            // mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
