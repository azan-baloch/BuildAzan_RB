// VerificationService.java
package com.buildazan.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerificationService {
    
    private static final int EXPIRATION_MINUTES = 10;
    private final EmailService emailService;
    private final UserService userService;

    public VerificationService(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    public String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }

    public String generateOTP() {
        return String.format("%06d", (int) (Math.random() * 900000) + 100000);
    }

    public void sendVerificationLink(String email) throws Exception {
        System.out.println("Verfication link triggered");
        String code = generateVerificationCode();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        
        userService.updateCodeAndExpiryByEmail(email, code, expiration);
        emailService.sendVerificationEmail(email, code);
        log.info("Verification link sent to {}", email);
    }

    public void sendOTPById(String userId, String email) throws Exception {
        System.out.println("Verfication OTP triggered");
        String otp = generateOTP();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        
        userService.updateCodeAndExpiryById(userId, otp, expiration);
        emailService.sendOtpEmail(email, otp);
        log.info("OTP sent to {}", email);
    } 
    public void sendOTPByEmail(String email) throws Exception {
        System.out.println("Verfication OTP triggered");
        String otp = generateOTP();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
        
        userService.updateCodeAndExpiryByEmail(email, otp, expiration);;
        emailService.sendOtpEmail(email, otp);
        log.info("OTP sent to {}", email);
    } 

    public boolean validateCode(String storedCode, LocalDateTime expiration, String inputCode) {
        if (storedCode == null || expiration == null) return false;
        return storedCode.equals(inputCode) && LocalDateTime.now().isBefore(expiration);
    }

    public boolean isCodeExpired(LocalDateTime expiration) {
        return expiration == null || LocalDateTime.now().isAfter(expiration);
    }
}