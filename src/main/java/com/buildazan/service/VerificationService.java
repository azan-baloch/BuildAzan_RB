package com.buildazan.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;


@Service
public class VerificationService{
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;
	

	
	public String generateVerificationCode() {
		return UUID.randomUUID().toString();
	}

	
	public boolean sendVerificationCode(String toEmail, String subject) {
		String randomCode = generateVerificationCode();
		userService.updateVerificationCodeByEmail(toEmail, randomCode);
	    String verificationCode = randomCode + ":" + toEmail + ":" + LocalDateTime.now();
	    if (emailService.sendVerificationEmail(toEmail, subject, verificationCode)) {
	        return true;
	    } else {
	        return false;
	    }
	}

	
	public boolean verifyCode(String code) {
		return userService.existsByVerificationCode(code);
	}

	
	public boolean checkCodeExpiration(LocalDateTime verificationCodeLocalDateTime) {
		Duration duration = Duration.between(verificationCodeLocalDateTime, LocalDateTime.now());
		if (duration.toMinutes()>10) {
			return false;
		}
		return true;
	}

	


	


}
